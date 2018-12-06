import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { EmpresaContratoComponent } from './empresa-contrato.component';

describe('EmpresaContratoComponent', () => {
  let component: EmpresaContratoComponent;
  let fixture: ComponentFixture<EmpresaContratoComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ EmpresaContratoComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EmpresaContratoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
