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
  </style>
</custom-style>


`;

document.head.appendChild($_documentContainer.content);
