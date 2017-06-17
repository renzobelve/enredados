import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { DateUtils, DataUtils, EventManager } from 'ng-jhipster';
import { EnredadosTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { GamePlayerDetailComponent } from '../../../../../../main/webapp/app/entities/game-player/game-player-detail.component';
import { GamePlayerService } from '../../../../../../main/webapp/app/entities/game-player/game-player.service';
import { GamePlayer } from '../../../../../../main/webapp/app/entities/game-player/game-player.model';

describe('Component Tests', () => {

    describe('GamePlayer Management Detail Component', () => {
        let comp: GamePlayerDetailComponent;
        let fixture: ComponentFixture<GamePlayerDetailComponent>;
        let service: GamePlayerService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [EnredadosTestModule],
                declarations: [GamePlayerDetailComponent],
                providers: [
                    DateUtils,
                    DataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    GamePlayerService,
                    EventManager
                ]
            }).overrideTemplate(GamePlayerDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(GamePlayerDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(GamePlayerService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new GamePlayer(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.gamePlayer).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
