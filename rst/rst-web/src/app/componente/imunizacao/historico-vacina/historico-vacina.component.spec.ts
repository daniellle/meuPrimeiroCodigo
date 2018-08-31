import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { HistoricoVacinaComponent } from './historico-vacina.component';

describe('HistoricoVacinaComponent', () => {
  let component: HistoricoVacinaComponent;
  let fixture: ComponentFixture<HistoricoVacinaComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ HistoricoVacinaComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(HistoricoVacinaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
