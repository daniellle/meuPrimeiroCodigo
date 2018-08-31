import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SolicitarEmailComponent } from './solicitar-email.component';

describe('SolicitarEmailComponent', () => {
  let component: SolicitarEmailComponent;
  let fixture: ComponentFixture<SolicitarEmailComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SolicitarEmailComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SolicitarEmailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
