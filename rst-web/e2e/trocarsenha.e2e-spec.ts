import {OpenMedicalAngularPage} from "./app.po";
import { browser, element, by, ElementFinder, ExpectedConditions} from 'protractor';

var TrocarSenhaPage = function() { 

  this.inputSenhaAtual = element(by.css('#senhaAtual'));  
  this.inputSenhaNova = element(by.css('#senhanova'));  
  this.inputSenhaNovaConfirme = element(by.css('#senhanovaconfirme'));  

  this.buttonSalvar = element(by.css("#btnsalvar"));
  
  this.msg = null;
  this.msgErro = null;

};

describe('Funcionalidade: Permitir que o usuário altere a própria senha. Dado um usuário esteja ativo e o mesmo usuário esteja logado', () => {
    var trocarSenhaPage = new TrocarSenhaPage();

    beforeEach(() => {
        browser.waitForAngular();    
    });
    beforeAll(function () {
        browser.get('/cadastro/gerenciarperfil/trocarsenha/');
    });  

//     it('Cenário 1: O usuário deseja alterar a própria senha com sucesso \n Dado que o usuário esteja logado \n Quando ele preencher o campo "Senha" \n E preencher o campo "Repetir senha" com a mesma informação inserida no campo "Senha" \n Então eu devo ver "Senha alterada com sucesso."', function() {
//         browser.driver.sleep(5000);
//         browser.waitForAngula
//         trocarSenhaPage.inputSenhaAtual.click();
//         trocarSenhaPage.inputSenhaAtual.sendKeys('Qq!12346');
//         trocarSenhaPage.inputSenhaNova.click();
//         trocarSenhaPage.inputSenhaNova.sendKeys('Qq!12346');    
//         trocarSenhaPage.inputSenhaNovaConfirme.click();
//         trocarSenhaPage.inputSenhaNovaConfirme.sendKeys('Qq!12346'); 
//         //trocarSenhaPage.buttonSalvar.click();
//         //browser.driver.sleep(1000);     
//         // trocarSenhaPage.msg = element(by.css(".toast"));
//         // expect(trocarSenhaPage.msg.getText()).toBe("Senha alterada com sucesso"); 

//         var EC = ExpectedConditions;
//         trocarSenhaPage.buttonSalvar.click()
//         .then(function(){
//             browser.ignoreSynchronization = true;       
//             browser.waitForAngular(); 
//             trocarSenhaPage.msg = element(by.css(".toast"));
//             //browser.driver.sleep(300);
//             browser.wait(EC.visibilityOf(trocarSenhaPage.msg), 1000) //wait until toast is displayed
//             .then(function(){
//                 expect(trocarSenhaPage.msg.getText()).toEqual('Senha alterada com sucesso');
//             });
//         });
        
//    }); 

   it('Cenário 2: O usuário deseja alterar a própria senha sem sucesso \n Dado que o usuário esteja logado \n Quando ele preencher o campo "Senha" \n E não preencher o campo "Repetir senha" com a mesma informação inserida no campo "Senha" \n Então eu devo ver "As senhas devem ser iguais."', function() {
        browser.driver.sleep(5000);
        browser.waitForAngula   
        trocarSenhaPage.inputSenhaAtual.click();
        trocarSenhaPage.inputSenhaAtual.sendKeys('Qq!12346');
        trocarSenhaPage.inputSenhaNova.click();
        trocarSenhaPage.inputSenhaNova.sendKeys('Qq!12346');    
        trocarSenhaPage.inputSenhaNovaConfirme.click();
        trocarSenhaPage.inputSenhaNovaConfirme.sendKeys('Qq!12345'); 
        trocarSenhaPage.inputSenhaNova.click();
        browser.driver.sleep(1000); 
        browser.ignoreSynchronization = true;       
        browser.waitForAngular();                  

        trocarSenhaPage.msgErro = element(by.css("#errorconfirmesenha"));
        expect(trocarSenhaPage.msgErro.getText()).toBe("Confirme a Senha Corretamente."); 
    });    
  });