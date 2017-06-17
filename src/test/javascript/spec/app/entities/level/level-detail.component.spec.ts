import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { DateUtils, DataUtils, EventManager } from 'ng-jhipster';
import { EnredadosTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { LevelDetailComponent } from '../../../../../../main/webapp/app/entities/level/level-detail.component';
import { LevelService } from '../../../../../../main/webapp/app/entities/level/level.service';
import { Level } from '../../../../../../main/webapp/app/entities/level/level.model';

describe('Component Tests', () => {

    describe('Level Management Detail Component', () => {
        let comp: LevelDetailComponent;
        let fixture: ComponentFixture<LevelDetailComponent>;
        let service: LevelService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [EnredadosTestModule],
                declarations: [LevelDetailComponent],
                providers: [
                    DateUtils,
                    DataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    LevelService,
                    EventManager
                ]
            }).overrideTemplate(LevelDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(LevelDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(LevelService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Level(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.level).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
