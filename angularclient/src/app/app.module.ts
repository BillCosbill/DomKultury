import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {LoginComponent} from './login/login.component';
import {FormsModule} from '@angular/forms';
import {HttpClientModule} from '@angular/common/http';
import {RegisterComponent} from './register/register.component';
import {HomeComponent} from './home/home.component';
import {RoomsComponent} from './rooms/rooms.component';
import {ContactComponent} from './contact/contact.component';
import {EventsComponent} from './events/events.component';
import {NgxPaginationModule} from 'ngx-pagination';
import {EventDetailsComponent} from './event-details/event-details.component';
import {authInterceptorProviders} from './_helpers/auth.interceptor';
import {AdministratorPanelComponent} from './administrator-panel/administrator-panel.component';
import {
  DayService,
  MonthAgendaService,
  MonthService,
  RecurrenceEditorAllModule,
  ScheduleAllModule,
  WeekService,
  WorkWeekService
} from '@syncfusion/ej2-angular-schedule';
import {ImageComponent} from './image/image.component';
import {AddRoomComponent} from './add-room/add-room.component';
import { RoomDetailsComponent } from './room-details/room-details.component';
import { AdministratorPanelActivationsComponent } from './administrator-panel-activations/administrator-panel-activations.component';
import { SubjectsComponent } from './subjects/subjects.component';
import { SubjectDetailsComponent } from './subject-details/subject-details.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    RegisterComponent,
    HomeComponent,
    RoomsComponent,
    ContactComponent,
    EventsComponent,
    EventDetailsComponent,
    AdministratorPanelComponent,
    ImageComponent,
    AddRoomComponent,
    RoomDetailsComponent,
    AdministratorPanelActivationsComponent,
    SubjectsComponent,
    SubjectDetailsComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    NgxPaginationModule,
    RecurrenceEditorAllModule,
    ScheduleAllModule
  ],
  providers: [authInterceptorProviders, DayService, WeekService, WorkWeekService, MonthService, MonthAgendaService],
  bootstrap: [AppComponent]
})
export class AppModule {
}
