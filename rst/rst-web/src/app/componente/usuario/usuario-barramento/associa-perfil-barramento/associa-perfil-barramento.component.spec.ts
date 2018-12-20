import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AssociaPerfilBarramentoComponent } from './associa-perfil-barramento.component';

describe('AssociaPerfilBarramentoComponent', () => {
  let component: AssociaPerfilBarramentoComponent;
  let fixture: ComponentFixture<AssociaPerfilBarramentoComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AssociaPerfilBarramentoComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AssociaPerfilBarramentoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
