import {OpenMedicalAngularPage} from "./app.po";
import { browser, element, by, ElementFinder, } from 'protractor';

var UnidadePage = function() { 
  this.tituloLabel = element(by.css(".titulo-pagina"));
  this.buttonPesquisar = element.all(by.css(".btn")).get(0);
  this.inputCNPJ = element(by.name('cnpj'));

  this.msgErro = null;
  
  this.inputRazaoSocial = element(by.css('#razaoSocial'));  
  this.tabledata = element.all(by.css(".table"));
  // get rows 
  this.rows = this.tabledata.all(by.tagName("tr"));
  // get cell values
  this.cells = this.rows.all(by.tagName("td"));  

  this.buttonNovo = element.all(by.css(".btn-primary")).get(1);
  
};

var UnidadePreviewPage = function() { 
  this.buttonDadosBasicos = element.all(by.css(".img_dadosbasico")).get(0);
  this.buttonProdServ = element.all(by.css(".img_produtoservico")).get(0);
};

var UnidadeEditPage = function() { 
  this.buttonCad = element(by.css(".btn-cadastro"));
};

var UnidadeCadPage = function() { 
  this.buttonCad = element.all(by.css(".btn-cadastro")).get(0);
};

describe('Teste atributos da pagina de Buscar Unidade', () => {
  var unidadePage = new UnidadePage();

  beforeEach(() => {
    browser.waitForAngular();    
  });
  beforeAll(function () {
    browser.get('/cadastro/uat/');
  });  

  it('A pagina de Unidade contem titulo !', function() {  
    expect(unidadePage.tituloLabel.isDisplayed()).toBe(true);    
  }); 

  it('A pagina de Unidade contem o botao Para Cadastrar Unidade !', function() {  
    expect(unidadePage.buttonNovo.isDisplayed()).toBe(true);     
  });   

  it('Ao pressionar Pesquisar sem preencher o formulario deve aparece uma msg de erro !', function() {
    expect(unidadePage.buttonPesquisar.isDisplayed()).toBe(true);    
  });  
 
  it('Ao preencher cpnj invalido deve aparecer uma msg de erro !', function() {
    browser.driver.sleep(3000);
    unidadePage.inputCNPJ.clear();       
    unidadePage.inputCNPJ.click();
    unidadePage.inputCNPJ.sendKeys('AAA1234');  
    unidadePage.buttonPesquisar.click();
    browser.driver.sleep(1000); 
    browser.ignoreSynchronization = true;       
    browser.waitForAngular();
    unidadePage.msgErro = element(by.css(".toast"));

    expect(unidadePage.msgErro.getText()).toBe("Por favor informar o CNPJ completo");            
  });   

  it('Ao preencher cpnj inexistente deve aparecer uma msg indicando que nenhum registro foi encontrado !', function() {
    browser.driver.sleep(3000);
    unidadePage.inputCNPJ.clear();       
    unidadePage.inputCNPJ.click();
    unidadePage.inputCNPJ.sendKeys('01.838.723/0139-62');    
    unidadePage.buttonPesquisar.click();
    browser.driver.sleep(5000); 
    browser.ignoreSynchronization = true;       
    browser.waitForAngular();
    unidadePage.msgErro = element(by.css(".toast"));

    expect(unidadePage.msgErro.getText()).toBe("Nenhum registro encontrado");            
  }); 

});

describe('Teste de resultados da busca por Unidade', () => {
  var unidadePage = new UnidadePage();

  beforeEach(() => {
    browser.waitForAngular();  
    //browser.get('/cadastro/uat/');  
  });
  beforeAll(function () {
    browser.get('/cadastro/uat/');
  });  

  it('Ao pesquisar por "a" deve aparecer uma tabela com registro e o primeiro registro em desenvolvimento e: "ATENDIMENTOS A UNIDADES - LUCAIA" !', function() {  
    browser.driver.sleep(1000);
    unidadePage.inputCNPJ.clear();   
    unidadePage.inputRazaoSocial.click();
    unidadePage.inputRazaoSocial.sendKeys('ATENDIMENTOS A UNIDADES - LUCAIA'); 
    unidadePage.buttonPesquisar.click();          
    browser.driver.sleep(15000);        
    browser.ignoreSynchronization = true;       
    browser.waitForAngular();     
    expect(unidadePage.cells.get(0).getText()).toEqual("ATENDIMENTOS A UNIDADES - LUCAIA");  
   });
});

describe('Teste do Preview de Unidade', () => {
  var unidadePage = new UnidadePage();
  var unidadePreviewPage = new UnidadePreviewPage(); 
  var unidadeEditPage = new UnidadeEditPage();   

  beforeEach(() => {
    browser.waitForAngular();  
  });
  beforeAll(function () {
    browser.get('/cadastro/uat/');
  });  

  it('Ao clicar em um elemento da lista após a busca deve aparecer a tela de Preview da Unidade Selecionada e o botao de Cadastro Basico deve aparecer" !', function() {  
    browser.driver.sleep(1000);  
    unidadePage.inputRazaoSocial.click();
    unidadePage.inputRazaoSocial.sendKeys('ATENDIMENTOS A UNIDADES - LUCAIA'); 
    unidadePage.buttonPesquisar.click();          
    browser.driver.sleep(12000);        
    browser.ignoreSynchronization = true;       
    browser.waitForAngular();

    unidadePage.cells.get(0).click();
    browser.driver.sleep(1000);        
    browser.ignoreSynchronization = true;       
    browser.waitForAngular();    
    
    expect(unidadePreviewPage.buttonDadosBasicos.isDisplayed()).toBe(true);  
   });

   it('Ao clicar em um elemento da lista após a busca deve aparecer a tela de Preview da Unidade Selecionada e o botao de Produtos e Servicos deve aparecer" !', function() {  

    browser.waitForAngular();    
    expect(unidadePreviewPage.buttonProdServ.isDisplayed()).toBe(true); 
   });   

   it('Na tela de Preview de Unidade ao clicar em cadastro Basico deve Aparecer o formulario de Edicao da Unidade !', function() {  
    browser.driver.sleep(2000);
    browser.waitForAngular(); 
    unidadePreviewPage.buttonDadosBasicos.click(); 
    browser.driver.sleep(1000);        
    browser.ignoreSynchronization = true;       
    browser.waitForAngular();  

    expect(unidadeEditPage.buttonCad.isDisplayed()).toBe(true); 
   });  
});

describe('Teste do Cadastro de Unidade', () => {
  var unidadePage = new UnidadePage(); 
  var unidadeCadPage = new UnidadeCadPage();

  beforeEach(() => {
    browser.waitForAngular();  
  });
  beforeAll(function () {
    browser.get('/cadastro/uat/');
  });  

  it('Ao clicar no botao Novo deve aparecer o Formulario de Cadastro de Unidade" !', function() {  
    browser.driver.sleep(1500);    
    browser.waitForAngular();
    unidadePage.buttonNovo.click();
    browser.driver.sleep(2500);        
    browser.ignoreSynchronization = true;       
    browser.waitForAngular();     


    //expect(true).toBe(true);
    expect(unidadeCadPage.buttonCad.isDisplayed()).toBe(true); 

  });    
});