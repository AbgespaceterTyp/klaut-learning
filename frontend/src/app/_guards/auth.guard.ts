import { Injectable } from '@angular/core';
import { Router, CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { tap } from 'rxjs/operators';
import { AuthenticationService } from '../_services';

@Injectable()
export class AuthGuard implements CanActivate {

    constructor(private router: Router, private authenticationService: AuthenticationService) { }

    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        return this.authenticationService.callUserMe()
            .pipe(
                tap(
                    data => { },
                    error => {
                        // not logged in so redirect to login page with the return url                                                                     
                        this.router.navigate(['/login'], { queryParams: { returnUrl: state.url } });
                    }
                )
            )
            .map(() => true);
    }
}