import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { VacinaGridComponent } from './vacina-grid.component';

describe('VacinaGridComponent', () => {
  let component: VacinaGridComponent;
  let fixture: ComponentFixture<VacinaGridComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ VacinaGridComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(VacinaGridComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
