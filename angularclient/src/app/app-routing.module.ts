import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {LoginComponent} from './login/login.component';
import {RegisterComponent} from './register/register.component';
import {HomeComponent} from './home/home.component';
import {RoomsComponent} from './rooms/rooms.component';
import {ContactComponent} from './contact/contact.component';
import {EventsComponent} from './events/events.component';
import {EventDetailsComponent} from './event-details/event-details.component';
import {AdministratorPanelComponent} from './administrator-panel/administrator-panel.component';
import {AdminguardService} from './_guards/adminguard.service';
import {ImageComponent} from './image/image.component';
import {AddRoomComponent} from './add-room/add-room.component';
import {RoomDetailsComponent} from './room-details/room-details.component';
import {AdministratorPanelActivationsComponent} from './administrator-panel-activations/administrator-panel-activations.component';


const routes: Routes = [
  {path: 'home', component: HomeComponent},
  {path: 'events', component: EventsComponent},
  {path: 'event/:id', component: EventDetailsComponent, pathMatch: 'full'},
  {path: 'room/:id', component: RoomDetailsComponent, pathMatch: 'full'},
  {path: 'rooms', component: RoomsComponent},
  {path: 'contact', component: ContactComponent},
  {path: 'login', component: LoginComponent},
  {path: 'register', component: RegisterComponent},
  {path: 'addRoom', component: AddRoomComponent},
  {path: 'image', component: ImageComponent},
  {path: 'admin_panel', component: AdministratorPanelComponent, canActivate: [AdminguardService]},
  {path: 'admin_panel_activations', component: AdministratorPanelActivationsComponent, canActivate: [AdminguardService]},
  {path: '', redirectTo: 'home', pathMatch: 'full'}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
