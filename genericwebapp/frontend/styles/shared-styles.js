// eagerly import theme styles so as we can override them
import '@vaadin/vaadin-lumo-styles/all-imports';

const $_documentContainer = document.createElement('template');

$_documentContainer.innerHTML = `
<custom-style>
  <style>
    html {
      --lumo-primary-color: hsl(161, 100%, 34%);
      --lumo-primary-text-color: hsl(161, 100%, 34%);
    }

    [theme~="dark"] {
      --lumo-base-color: hsl(214, 0%, 24%);
      --lumo-primary-color: hsl(161, 100%, 34%);
      --lumo-primary-text-color: hsl(161, 100%, 34%);
    }

    .grid-view {
      align-items: center;
      height: 100%;
      width: 100%
    }

    .grid-view-top-layout {
      width: 100%;
      display: flex;
      flex-direction: row;
    }

    .grid-view-filter {
      setMinWidth: 7em;
      flex-grow: 1;
    }

    .grid-view-add-entity-button {
      flex-grow: 0;
    }

    .grid-view-scroll-layout {
      width: 100%;
      height: unset;
      overflow-y: auto;
      padding: 0;
    }

    .grid-view-entity-grid {
      overflow-y: inherit;
    }

    .grid-view-editor-dialog-body {
      height: unset;
      padding: 0;
      margin: 0;
    }

    .grid-view-editor-dialog-top-layout {
      padding: 0;
      margin: 0;
      flex-grow: 1;
      width: 100%;
      height: unset;
      overflow-y: auto;
    }

    .grid-view-editor-dialog-bottom-layout {
      align-items: center;
      width: 100%;
      flex-grow: 0;
    }

    .grid-view-editor-save-button, .grid-view-editor-delete-button, .grid-view-editor-cancel-button {
      flex-grow: 1;
      margin-top: 0;
      margin-bottom: 0;
      min-width: 80px;
    }
  </style>
</custom-style>


`;

document.head.appendChild($_documentContainer.content);
