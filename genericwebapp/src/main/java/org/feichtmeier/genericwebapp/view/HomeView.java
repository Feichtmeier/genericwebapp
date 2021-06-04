package org.feichtmeier.genericwebapp.view;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.spring.annotation.VaadinSessionScope;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;

import org.feichtmeier.genericwebapp.entity.Article;
import org.feichtmeier.genericwebapp.security.SecurityUtils;
import org.feichtmeier.genericwebapp.service.ArticleImageService;
import org.feichtmeier.genericwebapp.service.ArticleService;
import org.feichtmeier.genericwebapp.service.UserService;
import org.feichtmeier.genericwebapp.view.constants.ViewNames;
import org.springframework.stereotype.Component;
import org.vaadin.maxime.MarkdownArea;
import org.vaadin.maxime.StringUtil;

@CssImport("./styles/views/home-view.css")
@Component
@VaadinSessionScope
public class HomeView extends AbstractView implements EntityEditor<Article> {

    private static final long serialVersionUID = -2333684897315095897L;

    // UI Fields
    private final HorizontalLayout viewTopLayout;
    private final VerticalLayout viewBottomLayout;
    private final TextField viewArticleFilter;
    private final Button viewNewArticleButton;
    private final Dialog articleEditorDialog;
    private final Button dialogSaveButton, dialogCancelButton, dialogDeleteButton;
    private final VerticalLayout dialogTopLayout;
    private final TextField dialogArticleTitleTextField;
    private final DateTimePicker dialogArticleDateTextField;
    private final Upload imageUpload;
    private final MarkdownArea markdownArea;
    private final HorizontalLayout dialogBottomLayout;
    private final VerticalLayout dialogBody;
    private final Binder<Article> articleBinder;
    // Markdown
    private final Parser parser = Parser.builder().build();
    private final HtmlRenderer renderer = HtmlRenderer.builder().build();
    // Data Fields
    private Article currentEntity;
    private final UserService userService;
    private final ArticleService articleService;
    private final ArticleImageService articleImageService;

    public HomeView(UserService userService, ArticleService articleService, ArticleImageService articleImageService) {

        this.userService = userService;
        this.articleService = articleService;
        this.articleImageService = articleImageService;

        // View Top
        viewTopLayout = new HorizontalLayout();
        viewArticleFilter = new TextField("", "Search ...");
        viewArticleFilter.setValueChangeMode(ValueChangeMode.EAGER);
        viewArticleFilter.addValueChangeListener(e -> {

        });
        viewNewArticleButton = new Button(VaadinIcon.PLUS.create(), e -> {
            editEntity(new Article(LocalDateTime.now(), "", "", "",
                    userService.findByUsername(SecurityUtils.getUsername())));
        });
        viewTopLayout.add(viewNewArticleButton, viewArticleFilter);

        viewBottomLayout = new VerticalLayout();

        add(viewTopLayout, viewBottomLayout);

        // Edit Dialog
        articleBinder = new Binder<>(Article.class);
        articleEditorDialog = new Dialog();
        articleEditorDialog.setMaxWidth("400px");
        // Dialog top
        dialogTopLayout = new VerticalLayout();
        dialogArticleTitleTextField = new TextField("Title");
        dialogArticleDateTextField = new DateTimePicker();
        // dialogArticleDateTextField.setLocale(Locale.GERMANY);
        articleBinder.bind(dialogArticleDateTextField, "timeStamp");
        markdownArea = new MarkdownArea();
        markdownArea.getInput().setHeight("200px");
        imageUpload = new Upload();
        dialogTopLayout.add(dialogArticleDateTextField, dialogArticleTitleTextField, markdownArea, imageUpload);

        // Dialog bottom
        dialogSaveButton = new Button("", VaadinIcon.CHECK.create(), e -> {
            if (articleBinder.validate().isOk()) {
                if(currentEntity.getTimeStamp() == null) {
                    currentEntity.setTimeStamp(LocalDateTime.now());
                }

                articleService.save(currentEntity);
                refreshArticlesInView();

                Notification.show("Saved Article " + currentEntity.getTitle());
                goBackToView();
            } else {
                Notification.show("NOT saved Article " + currentEntity.getTitle());
            }
        });
        dialogCancelButton = new Button("", VaadinIcon.CLOSE.create(), e -> {
            goBackToView();
        });
        dialogDeleteButton = new Button("", VaadinIcon.TRASH.create(), e -> {
            articleService.delete(currentEntity);
            Notification.show("Deleted " + currentEntity.getTitle());
            goBackToView();
        });
        dialogBottomLayout = new HorizontalLayout(dialogSaveButton, dialogCancelButton, dialogDeleteButton);
        // Add to dialog
        dialogBody = new VerticalLayout(dialogTopLayout, dialogBottomLayout);
        articleEditorDialog.add(dialogBody);
        articleEditorDialog.setCloseOnEsc(false);
        articleEditorDialog.setCloseOnOutsideClick(false);
        // Bind data in dialog
        articleBinder.forField(dialogArticleTitleTextField).asRequired("Must chose an article title!")
                .bind(Article::getTitle, Article::setTitle);
        articleBinder.bind(markdownArea.getInput(), "textBody");
        refreshArticlesInView();
    }

    private void refreshArticlesInView() {
        viewBottomLayout.removeAll();
        articleService.findAll().forEach((article) -> addArticleToView(article));
    }

    private void addArticleToView(Article article) {
        final VerticalLayout aVerticalLayout = new VerticalLayout();
        final H2 title = new H2(article.getTitle());
        final Button editButton = new Button(VaadinIcon.EDIT.create(), e -> {
            editEntity(article);
        });
        editButton.setVisible(userService.isViewEditable(userService.findByUsername(SecurityUtils.getUsername()),
                ViewNames.ARTICLE_EDIT_BUTTON));
        title.setClassName("article-title");
        // final Text text = new Text(body);
        final Div markdownOutput = new Div();
        String text = markdownArea.getValue().isEmpty() ? "*Nothing to preview*" : markdownArea.getValue();
        addMarkdown(text, markdownOutput);
        final Label dateText = new Label(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).format(article.getTimeStamp()));
        dateText.setClassName("article-date");
        aVerticalLayout.add(dateText, title, markdownOutput, editButton);
        aVerticalLayout.setClassName("article");
        viewBottomLayout.add(aVerticalLayout);
    }

    private void addMarkdown(String value, Div previewView) {
        String html = String.format("<div>%s</div>", parseMarkdown(StringUtil.getNullSafeString(value)));
        Html item = new Html(html);
        previewView.removeAll();
        previewView.add(item);
    }

    private String parseMarkdown(String value) {
        com.vladsch.flexmark.ast.Node text = parser.parse(value);
        return renderer.render(text);
    }

    @Override
    public void editEntity(Article article) {
        if (article == null) {
            articleEditorDialog.close();
            return;
        }
        articleEditorDialog.open();
        this.currentEntity = article;
        articleBinder.setBean(currentEntity);
    }

    private void goBackToView() {
        articleEditorDialog.close();
        // refreshRoles();
        // refreshPermissions();
        viewArticleFilter.clear();
    }

    private void showArticleDetails(Article article) {

    }

    @Override
    public void linkComponentsToCss() {
        setId("home-view");
        setClassName("grid-view");
        viewTopLayout.addClassName("grid-view-top-layout");
        viewBottomLayout.addClassName("grid-view-scroll-layout");
        viewArticleFilter.addClassName("grid-view-filter");
        viewNewArticleButton.getElement().getThemeList().add("primary");
        viewNewArticleButton.addClassName("grid-view-add-entity-button");
        dialogBody.addClassName("grid-view-editor-dialog-body");
        dialogBody.addClassName("article-editor");
        dialogTopLayout.addClassName("grid-view-editor-dialog-top-layout");
        dialogBottomLayout.addClassName("grid-view-editor-dialog-bottom-layout");
        dialogSaveButton.getElement().getThemeList().add("primary");
        dialogSaveButton.addClassName("grid-view-editor-save-button");
        dialogCancelButton.addClassName("grid-view-editor-cancel-button");
        dialogDeleteButton.getElement().getThemeList().add("error");
        dialogDeleteButton.addClassName("grid-view-editor-delete-button");
        dialogArticleTitleTextField.addClassName("article-editor-title");
        dialogArticleDateTextField.addClassName("article-editor-date");
    }
}