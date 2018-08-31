import {OpenMedicalAngularPage} from "./app.po";
import { browser, element, by, ElementFinder, ExpectedConditions} from 'protractor';

var EditarProfissionalPage  = function() { 

    this.selectConselhoRegional = element(by.css('#conselhoRegional'));
    this.buttonSalvar = element(by.css("#btnsalvar"));
    
    this.msg = null;
    this.msgErro = null;
  
};

describe('Funcionalidade: Permitir que o usuário altere o conselho Regional de um Profissional.', () => {
    var editarProfissionalPage = new EditarProfissionalPage();

    beforeEach(() => {
        browser.waitForAngular();
    });
    beforeAll(function () {
        browser.get('/cadastro/profissional/1/');
    });

    // tslint:disable-next-line:max-line-length
    it('Cenário 1: O usuário deseja alterar o conselho Regional \n Dado que o usuário esteja logado \n Quando ele alterar o campo conselho Regional  \n e apertar o botao Salvar \n Então eu devo ver "alterado com sucesso."', function() {
        browser.driver.sleep(5000);
        browser.waitForAngula
        editarProfissionalPage.selectConselhoRegional.sendKeys('ANAMT');

        var EC = ExpectedConditions;
        editarProfissionalPage.buttonSalvar.click()
        .then(function(){
            browser.ignoreSynchronization = true;
            browser.waitForAngular(); 
            editarProfissionalPage.msg = element(by.css(".toast"));
            browser.wait(EC.visibilityOf(editarProfissionalPage.msg), 3000)
            .then(function(){
                expect(editarProfissionalPage.msg.getText()).toEqual('Operação realizada com sucesso!');
            });
        });

    });
});