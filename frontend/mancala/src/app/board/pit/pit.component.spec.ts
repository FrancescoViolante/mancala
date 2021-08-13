import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PitComponent } from './pit.component';

describe('PitComponent', () => {
  let component: PitComponent;
  let fixture: ComponentFixture<PitComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PitComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PitComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
