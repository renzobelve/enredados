import {Component, OnInit} from '@angular/core';
import {Router, ActivatedRouteSnapshot, NavigationEnd, RoutesRecognized} from '@angular/router';

import {Title} from '@angular/platform-browser';
import {StateStorageService, Principal} from '../../shared';

@Component({
    selector: 'jhi-main',
    templateUrl: './main.component.html'
})
export class JhiMainComponent implements OnInit {

    constructor(
        private principal: Principal,
        private titleService: Title,
        private router: Router,
        private $storageService: StateStorageService,
    ) {
    }

    private getPageTitle(routeSnapshot: ActivatedRouteSnapshot) {
        let title: string = (routeSnapshot.data && routeSnapshot.data['pageTitle']) ? routeSnapshot.data['pageTitle'] : 'enredadosApp';
        if (routeSnapshot.firstChild) {
            title = this.getPageTitle(routeSnapshot.firstChild) || title;
        }
        title = 'Enredados | La diversión de aprender';
        return title;
    }

    ngOnInit() {
        this.router.events.subscribe((event) => {
            if (event instanceof NavigationEnd) {
                this.titleService.setTitle(this.getPageTitle(this.router.routerState.snapshot.root));
            }
        });
    }

    isAuthenticated() {
        return this.principal.isAuthenticated();
    }
}
