import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CadastrarVacinaComponent } from './cadastrar-vacina.component';

describe('CadastrarVacinaComponent', () => {
  let component: CadastrarVacinaComponent;
  let fixture: ComponentFixture<CadastrarVacinaComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CadastrarVacinaComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CadastrarVacinaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
