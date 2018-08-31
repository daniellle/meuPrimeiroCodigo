const {SpecReporter} = require('jasmine-spec-reporter');

exports.config = {
  allScriptsTimeout: 11000,
  specs: [
    //'./e2e/**/*.e2e-spec.ts',
    //'./e2e/**/trocarsenha.e2e-spec.ts', //funciona para usuario elson - 65020081515
    //'./e2e/**/unidadevadmin.e2e-spec.ts', //funciona para usuario carol - 37139762520
    './e2e/**/formprofissional.e2e-spec.ts',
    './e2e/**/formtrabalhador.e2e-spec.ts',
  ],
  capabilities: {
    'browserName': 'chrome'
  },
  directConnect: true,
  baseUrl: 'http://localhost:4200/cadastro/',
  framework: 'jasmine',
  jasmineNodeOpts: {
    showColors: true,
    defaultTimeoutInterval: 30000,
    print: function () {
    }
  },
  beforeLaunch: function () {
    require('ts-node').register({
      project: 'e2e/tsconfig.e2e.json'
    });
  },
  onPrepare() {
    jasmine.getEnv().addReporter(new SpecReporter({spec: {displayStacktrace: true}}));
  }
};
