import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {LoginComponent} from './login/login.component';
import {RegisterComponent} from './register/register.component';
import {HomeComponent} from './home/home.component';
import {RoomsComponent} from './rooms/rooms.component';
import {ContactComponent} from './contact/contact.component';
import {AdministratorPanelComponent} from './administrator-panel/administrator-panel.component';
import {AdminguardService} from './_guards/adminguard.service';
import {ImageComponent} from './image/image.component';
import {AddRoomComponent} from './add-room/add-room.component';
import {RoomDetailsComponent} from './room-details/room-details.component';
import {AdministratorPanelActivationsComponent} from './administrator-panel-activations/administrator-panel-activations.component';
import {SubjectsComponent} from './subjects/subjects.component';
import {SubjectDetailsComponent} from './subject-details/subject-details.component';
import {LessonDetailsComponent} from './lesson-details/lesson-details.component';
import {SubjectDatailsStudentsComponent} from './subject-datails-students/subject-datails-students.component';
import {LessonDetailsAttendanceComponent} from './lesson-details-attendance/lesson-details-attendance.component';
import {DiaryComponent} from './diary/diary.component';
import {TeacherguardService} from './_guards/teacherguard.service';
import {DiaryStudentsComponent} from './diary-students/diary-students.component';


const routes: Routes = [
  {path: 'home', component: HomeComponent},
  {path: 'subjects', component: SubjectsComponent},
  {path: 'subject/:id', component: SubjectDetailsComponent, pathMatch: 'full'},
  {path: 'subject/:id/students', component: SubjectDatailsStudentsComponent, pathMatch: 'full', canActivate: [TeacherguardService]},
  {path: 'subject/:id2/lesson/:id', component: LessonDetailsComponent, pathMatch: 'full', canActivate: [TeacherguardService]},
  {path: 'subject/:id2/lesson/:id/attendance', component: LessonDetailsAttendanceComponent, pathMatch: 'full', canActivate: [TeacherguardService]},
  {path: 'diary', component: DiaryComponent, canActivate: [TeacherguardService]},
  {path: 'diary/students', component: DiaryStudentsComponent, canActivate: [TeacherguardService]},
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
