import {OpenMedicalAngularPage} from "./app.po";
import { browser, element, by, ElementFinder, ExpectedConditions} from 'protractor';

var EditarTrabalhadorPage  = function() { 

    this.vidaAtiva = element(by.css('#vidaAtiva'));
    this.semvidaAtiva = element(by.css('#semvidaAtiva')); 
  
};

describe('Funcionalidade: Visualizar a vida ativa do trabalhador.', () => {
    var editarTrabalhadorPage = new EditarTrabalhadorPage();

    beforeEach(() => {
        browser.waitForAngular();
    });
    beforeAll(function () {
        browser.get('/cadastro/trabalhador/4/cadastrar/');
    });

    it('Cenário: O usuário visualiza um trabalhador possui 1 registro de vida ativa \n  Quando o usuário visualizar os dados do "Trabalhador" \n  Então ele deve ver "1 Vida Ativa"."', function() {
        browser.driver.sleep(5000);
        browser.waitForAngula
        expect(editarTrabalhadorPage.vidaAtiva.isDisplayed()).toBe(true);
    });
});

describe('Funcionalidade: Visualizar a vida ativa do trabalhador.', () => {
    var editarTrabalhadorPage = new EditarTrabalhadorPage();

    beforeEach(() => {
        browser.waitForAngular();
    });
    beforeAll(function () {
        browser.get('/cadastro/trabalhador/1251/cadastrar/');
    });

    it('Cenário: O usuário visualiza um trabalhador possui dois registros de vida ativa \n  Quando o usuário visualizar os dados do "Trabalhador" \n  Então ele deve ver "2 Vida Ativa"."', function() {
        browser.driver.sleep(5000);
        browser.waitForAngula
        expect(editarTrabalhadorPage.vidaAtiva.isDisplayed()).toBe(true);
    });
});

describe('Funcionalidade: Visualizar a vida ativa do trabalhador.', () => {
    var editarTrabalhadorPage = new EditarTrabalhadorPage();

    beforeEach(() => {
        browser.waitForAngular();
    });
    beforeAll(function () {
        browser.get('/cadastro/trabalhador/7/cadastrar/');
    });

    it('Cenário: O usuário visualiza um trabalhador que nao possui dt_contrato_fim definido \n  Quando o usuário visualizar os dados do "Trabalhador" \n  Então ele deve ver "Sem Vida Ativa"."', function() {
        browser.driver.sleep(5000);
        browser.waitForAngula
        expect(editarTrabalhadorPage.semvidaAtiva.isDisplayed()).toBe(true);
    });
});