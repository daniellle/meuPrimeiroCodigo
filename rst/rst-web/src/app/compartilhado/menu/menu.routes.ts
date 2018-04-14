import {IMenu} from './menu.metadata';

export const ROTAS: IMenu[] = [
  {path: 'dashboard', title: 'Dashboard', icon: 'dashboard', class: ''},
  {path: 'user', title: 'User Profile', icon: 'person', class: ''},
  {path: 'table', title: 'Table List', icon: 'content_paste', class: ''},
  {path: 'typography', title: 'Typography', icon: 'library_books', class: ''},
  {path: 'icons', title: 'Icons', icon: 'bubble_chart', class: ''},
  {path: 'maps', title: 'Maps', icon: 'location_on', class: ''},
  {path: 'notifications', title: 'Notifications', icon: 'notifications', class: ''},
  {path: 'upgrade', title: 'Upgrade to PRO', icon: 'unarchive', class: 'active-pro'},
  {path: 'uat', title: 'Pesquisar UAT', icon: 'unarchive', class: ''},
  {path: 'empresa', title: 'Pesquisar Empresa', icon: 'unarchive', class: ''},
  {path: 'trabalhador', title: 'Pesquisa Trabalhador', icon: 'unarchive', class: ''},
  {path: 'departamentoregional', title: 'Pesquisar Departamento Regional', icon: 'unarchive', class: ''},
];
