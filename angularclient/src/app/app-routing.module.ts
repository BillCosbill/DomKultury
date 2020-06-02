import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {AppComponent} from "./app.component";
import {LoginComponent} from "./login/login.component";
import {RegisterComponent} from "./register/register.component";
import {HomeComponent} from "./home/home.component";
import {RoomsComponent} from "./rooms/rooms.component";
import {ContactComponent} from "./contact/contact.component";
import {EventsComponent} from "./events/events.component";
import {EventDetailsComponent} from "./event-details/event-details.component";


const routes: Routes = [
  { path: 'home', component: HomeComponent },
  { path: 'events', component: EventsComponent },
  { path: 'event/:id', component: EventDetailsComponent, pathMatch: 'full'},
  { path: 'rooms', component: RoomsComponent },
  { path: 'contact', component: ContactComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: '', redirectTo: 'home', pathMatch: 'full' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
