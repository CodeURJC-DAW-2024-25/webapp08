import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { UserDto } from "../dtos/User.dto";
import { PublicUserDto } from "../dtos/PublicUser.dto";
import { Observable, Subject } from "rxjs";
import { tap } from 'rxjs/operators';

const BASE_URL = "/api/v1/auth";

@Injectable({ providedIn: "root" })
export class LoginService {
  private logoutSubject = new Subject<void>();
  logout$ = this.logoutSubject.asObservable();
  public logged: boolean | undefined;
  public user: PublicUserDto | undefined;

  constructor(private http: HttpClient) {
    this.reqIsLogged();
  }

  public reqIsLogged() {
    this.http.get("/api/v1/users", { withCredentials: true }).subscribe(
      (response) => {
        this.user = response as PublicUserDto;
        this.logged = true;
      },
      (error) => {
        if (error.status != 404) {
          console.error(
            "Error when asking if logged: " + JSON.stringify(error)
          );
        }
      }
    );
  }

  public logIn(user: string, pass: string) {
    return this.http.post(
      BASE_URL + "/login",
      { username: user, password: pass },
      { withCredentials: true }
    );
  }

  public register(userDto: UserDto) {
    return this.http.post(BASE_URL + "/user", userDto, { withCredentials: true });
  }

  public logOut() {
    return this.http.post(BASE_URL + "/logout", {}, { withCredentials: true }).subscribe(
      () => {
        console.log("LOGOUT: Successfully");
        this.logged = false; 
        this.user = undefined; 
        this.logoutSubject.next();
      },
      (error) => {
        console.error("Error during logout:", error);
      }
    );
  }

  public isLogged() {
    return this.logged;
  }

  public setLogged(logged: boolean) {
    this.logged = logged;
  }

  public isAdmin() {
    return this.user && this.user.rols.indexOf("ADMIN") !== -1;
  }

  currentUser() {
    return this.user;
  }

  reqUser(): Observable<PublicUserDto> {
    return this.http.get<PublicUserDto>('/api/v1/users', { withCredentials: true }).pipe(
      tap(user => {
        this.user = user;
      })
    );
  }

  getUserId(): Observable<number> { // gives back the current user's id
    return new Observable((observer) => {
      if (this.user) {
        observer.next(this.user.id);
        observer.complete();
      } else {
        observer.error('User not available');
      }
    });
  }
}