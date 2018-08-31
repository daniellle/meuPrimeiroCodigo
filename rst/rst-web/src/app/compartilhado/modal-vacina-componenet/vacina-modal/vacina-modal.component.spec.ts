import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { VacinaModalComponent } from './vacina-modal.component';

describe('VacinaModalComponent', () => {
  let component: VacinaModalComponent;
  let fixture: ComponentFixture<VacinaModalComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ VacinaModalComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(VacinaModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
