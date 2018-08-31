import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TelaInicialVacinaComponent } from './tela-inicial-vacina.component';

describe('TelaInicialVacinaComponent', () => {
  let component: TelaInicialVacinaComponent;
  let fixture: ComponentFixture<TelaInicialVacinaComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TelaInicialVacinaComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TelaInicialVacinaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
