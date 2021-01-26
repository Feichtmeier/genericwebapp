package org.feichtmeier.genericwebapp.view;

import java.time.LocalDateTime;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.spring.annotation.VaadinSessionScope;

import org.feichtmeier.genericwebapp.entity.Article;
import org.feichtmeier.genericwebapp.security.SecurityUtils;
import org.feichtmeier.genericwebapp.service.ArticleImageService;
import org.feichtmeier.genericwebapp.service.ArticleService;
import org.feichtmeier.genericwebapp.service.UserService;
import org.springframework.stereotype.Component;

@CssImport("./styles/views/home-view.css")
@Component
@VaadinSessionScope
public class HomeView extends AbstractView {

    private static final long serialVersionUID = -2333684897315095897L;

    // UI Fields
    private final HorizontalLayout viewTopLayout;
    private final TextField viewArticleFilter;
    private final Button viewNewArticleButton;
    private final Dialog articleEditorDialog;
    private final Button dialogSaveButton, dialogCancelButton, dialogDeleteButton;
    private final FormLayout dialogTopLayout;
    private final TextField dialogArticleTitleTextField;
    private final HorizontalLayout dialogBottomLayout;
    private final VerticalLayout dialogBody;
    private final Binder<Article> articleBinder;
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
            editEntity(new Article(LocalDateTime.now(), "", "", null, userService.findByUsername(SecurityUtils.getUsername())));
        });
        viewTopLayout.add(viewNewArticleButton, viewArticleFilter);

        add(viewTopLayout);

        // Edit Dialog
        articleBinder = new Binder<>(Article.class);
        articleEditorDialog = new Dialog();
        // Dialog top
        dialogTopLayout = new FormLayout();
        dialogArticleTitleTextField = new TextField("Title");

        dialogTopLayout.add(dialogArticleTitleTextField);
        // Dialog bottom
        dialogSaveButton = new Button("", VaadinIcon.CHECK.create(), e -> {
            if (articleBinder.validate().isOk()) {
                articleService.save(currentEntity);
                Notification.show("Saved Role " + currentEntity.getTitle());
                goBackToView();
            } else {
                Notification.show("NOT saved Role " + currentEntity.getTitle());
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
        articleBinder.forField(dialogArticleTitleTextField).asRequired("Must chose a role name").bind(Article::getTitle,
                Article::setTitle);
    }

    public void editEntity(Article entity) {
        if (entity == null) {
            articleEditorDialog.close();
            return;
        }
        articleEditorDialog.open();
        this.currentEntity = entity;
        articleBinder.setBean(currentEntity);

        // refreshPermissions();
    }

    private void goBackToView() {
        articleEditorDialog.close();
        // refreshRoles();
        // refreshPermissions();
        viewArticleFilter.clear();
    }

    @Override
    public void linkComponentsToCss() {
        setId("home-view");
        viewTopLayout.addClassName("grid-view-top-layout");
        viewArticleFilter.addClassName("grid-view-filter");
        viewNewArticleButton.getElement().getThemeList().add("primary");
        viewNewArticleButton.addClassName("grid-view-add-entity-button");
        dialogBody.addClassName("grid-view-editor-dialog-body");
        dialogTopLayout.addClassName("grid-view-editor-dialog-top-layout");
        dialogBottomLayout.addClassName("grid-view-editor-dialog-bottom-layout");
        dialogSaveButton.getElement().getThemeList().add("primary");
        dialogSaveButton.addClassName("grid-view-editor-save-button");
        dialogCancelButton.addClassName("grid-view-editor-cancel-button");
        dialogDeleteButton.getElement().getThemeList().add("error");
        dialogDeleteButton.addClassName("grid-view-editor-delete-button");
    }
}