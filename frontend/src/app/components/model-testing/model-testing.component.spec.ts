import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ModelTestingComponent } from './model-testing.component';

describe('ModelTestingComponent', () => {
  let component: ModelTestingComponent;
  let fixture: ComponentFixture<ModelTestingComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ModelTestingComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ModelTestingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
