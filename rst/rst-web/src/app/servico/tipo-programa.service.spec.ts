import { TestBed, inject } from '@angular/core/testing';

import { TipoProgramaService } from './tipo-programa.service';

describe('TipoProgramaService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [TipoProgramaService]
    });
  });

  it('should be created', inject([TipoProgramaService], (service: TipoProgramaService) => {
    expect(service).toBeTruthy();
  }));
});
