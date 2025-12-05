import { Component } from '@angular/core';
import { TokenService } from 'src/app/services/token/token.service';

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.scss'],
})
export class MenuComponent {
  userFirstName: string = '';

  constructor(private tokenService: TokenService) {}
  ngOnInit() {
    const fullName = this.tokenService.fullName;
    if (fullName) {
      // Extract first name (everything before the first space)
      this.userFirstName = fullName.split(' ')[0];
    }

    const linkColor = document.querySelectorAll('.nav-link');
    linkColor.forEach((link) => {
      if (window.location.href.endsWith(link.getAttribute('href') || '')) {
        link.classList.add('active');
      }
      link.addEventListener('click', () => {
        linkColor.forEach((link) => {
          link.classList.remove('active');
        });
        link.classList.add('active');
      });
    });
  }
  logout() {
    localStorage.clear();
    window.location.reload();
  }
}
