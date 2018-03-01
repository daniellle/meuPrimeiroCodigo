// The file contents for the current environment will overwrite these during build.
// The build system defaults to the dev environment which uses `environment.ts`, but if you do
// `ng build --env=prod` then `environment.prod.ts` will be used instead.
// The list of which env maps to which file can be found in `.angular-cli.json`.

export const environment = {
    production: false,
    api_private: 'http://localhost:8080/rst/api/private',
    api_public: 'http://localhost:8080/rst/api/public',
    url_portal: 'http://localhost:8080',
    path_raiz_cadastro: 'cadastro',
    path_raiz: '',
    baseHref: '/',
    exibirMenu: true,
    exibirWS: true
};
