import { Routes, RouterModule } from '@angular/router';

import { HomeComponent } from './pages/home/home.component';
import { LoginComponent } from './pages/login/login.component';
import { RegisterComponent } from './pages/register/register.component';
import { ImpressumComponent } from './pages/impressum/impressum.component';
import { UserComponent } from './components/user/user.component';
import { OrganizationComponent } from './pages/organization/organization.component';

const appRoutes: Routes = [
    { path: 'home', component: HomeComponent },
    { path: 'login', component: LoginComponent },
    { path: 'register', component: RegisterComponent },
    { path: 'impressum', component: ImpressumComponent },
    { path: 'organization', component: OrganizationComponent },

    // otherwise redirect to home
    { path: '**', redirectTo: 'home' }

];

export const routing = RouterModule.forRoot(appRoutes);