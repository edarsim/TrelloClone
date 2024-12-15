import { Component } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { FormsModule } from '@angular/forms'; // Import FormsModule
import { Router, RouterModule, Routes } from '@angular/router';

@Component({
  selector: 'app-login',// Tag to use this component
  imports: [FormsModule, RouterModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css',
})
export class LoginComponent {
  username = '';
  password = '';

  constructor(private authService: AuthService, private router: Router) {} // Inject AuthService

  onSubmit(): void{

    this.authService.login(this.username, this.password).subscribe({
        next: (token) => {
          console.log('Login  successfull!');
          this.goToDashboard();
        }, // Success callback
        error: (err) => console.error('Login failed!', err), // Error callback
    });
  }

  goToDashboard(): void {
    this.router.navigate(['/dashboard']);
  }
}
