import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, NavigationEnd, Router} from '@angular/router';

@Component({
  moduleId: module.id,
  selector: 'app-rastro',
  templateUrl: './rastro.component.html',
  styleUrls: ['./rastro.component.css'],
})
export class RastroComponent implements OnInit {

  breadcrumbs: Object[];

  constructor(private router: Router, private route: ActivatedRoute) {
  }

  ngOnInit(): void {
    this.router.events.filter((event) => event instanceof NavigationEnd).subscribe(() => {
      this.breadcrumbs = [];
      let currentRoute = this.route.root;
      let  url = '';
      do {
        const childrenRoutes = currentRoute.children;
        currentRoute = null;
        childrenRoutes.forEach((route) => {
          if (route.outlet === 'primary') {
            const routeSnapshot = route.snapshot;
            url += '/' + routeSnapshot.url.map((segment) => segment.path).join('/');
            this.breadcrumbs.push({
              label: route.snapshot.data,
              url,
            });
            currentRoute = route;
          }
        });
      } while (currentRoute);
    });
    if (!this.breadcrumbs) {
      this.breadcrumbs = [];
      this.breadcrumbs.push({
        label: {title: 'Home'},
        url: '/#/',
      });
    }
  }

}
