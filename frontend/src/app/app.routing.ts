import { Routes, RouterModule } from '@angular/router';

import { HomeComponent } from './pages/home/home.component';
import { LoginComponent } from './pages/login/login.component';
import { RegisterComponent } from './pages/register/register.component';
import { UserComponent } from './user/user.component';
import { ImpressumComponent } from './pages/impressum/impressum.component';


const appRoutes: Routes = [
    { path: 'home', component: HomeComponent },
    { path: 'login', component: LoginComponent },
    { path: 'register', component: RegisterComponent },
    { path: 'user', component: UserComponent },
    { path: 'impressum', component: ImpressumComponent },
    

    // otherwise redirect to home
    { path: '**', redirectTo: 'home' }

];

export const routing = RouterModule.forRoot(appRoutes);