import {OpenMedicalAngularPage} from "./app.po";

describe('open-medical-angular App', () => {
  let page: OpenMedicalAngularPage;

  beforeEach(() => {
    page = new OpenMedicalAngularPage();
  });

  it('should display message saying app works', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('app works!');
  });
});
