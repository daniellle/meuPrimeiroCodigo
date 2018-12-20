import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RelatorioPerfisUsuariosComponent } from './relatorio-perfis-usuarios.component';

describe('RelatorioPerfisUsuariosComponent', () => {
  let component: RelatorioPerfisUsuariosComponent;
  let fixture: ComponentFixture<RelatorioPerfisUsuariosComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RelatorioPerfisUsuariosComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RelatorioPerfisUsuariosComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
