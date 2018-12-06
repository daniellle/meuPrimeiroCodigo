import { TestBed, inject } from '@angular/core/testing';

import { EmpresaContratoService } from './empresa-contrato.service';

describe('EmpresaContratoService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [EmpresaContratoService]
    });
  });

  it('should be created', inject([EmpresaContratoService], (service: EmpresaContratoService) => {
    expect(service).toBeTruthy();
  }));
});
