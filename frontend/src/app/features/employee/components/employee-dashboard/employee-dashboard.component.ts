import { Component } from '@angular/core';
import { HlmCardImports } from '@spartan-ng/ui-card-helm';
import Chart from 'chart.js/auto';
import { AuthService } from '../../../../shared/services/auth.service';
import { CustomerService } from '../../../../shared/services/customer.service';
import { InvoiceService } from '../../../../shared/services/invoice.service';
import { StatisticData } from '../../../../types/statistic-data';
import { User } from '../../../../types/user';

@Component({
  selector: 'app-employee-dashboard',
  standalone: true,
  imports: [HlmCardImports],
  templateUrl: './employee-dashboard.component.html',
  styleUrl: './employee-dashboard.component.css',
  host: {
    class: 'flex flex-col gap-6',
  },
})
export class EmployeeDashboardComponent {
  user: User | null = null;

  totalInvoices: number = 0;
  unpaidInvoices: number = 0;
  totalCustomers: number = 0;
  averageConsumption: StatisticData = {};
  invoiceStatusData: StatisticData = {};

  barChart: any;
  doughnutChart: any;
Object: any;

  constructor(
    private invoiceService: InvoiceService,
    private authService: AuthService,
    private customerService: CustomerService
  ) {}

  ngOnInit(): void {
    this.user = this.authService.getUserData();
    if (this.user) {
      this.fetchInvoiceStatistics();
    }
  }

  fetchInvoiceStatistics(): void {
    this.invoiceService.getAllInvoicesCount().subscribe({
      next: (response) => {
        this.totalInvoices = response.count;
      },
      error: (error) => {
        console.error('Error fetching all invoices count', error);
      },
    });

    this.invoiceService.getAllUnpaidInvoicesCount().subscribe({
      next: (response) => {
        this.unpaidInvoices = response.count;
      },
      error: (error) => {
        console.error('Error fetching unpaid invoices count', error);
      },
    });

    this.customerService.getTotalCustomersCount().subscribe({
      next: (response) => {
        this.totalCustomers = response.count;
      },
      error: (error) => {
        console.error('Error fetching customer unpaid invoices count', error);
      },
    });

    this.invoiceService.getAverageUnitConsumption().subscribe({
      next: (response) => {
        this.averageConsumption = response.data.consumptionData;
        this.createBarChart(); // Call createChart after fetching the data
      },
      error: (error) => {
        console.error('Error fetching average unit consumption', error);
      },
    });

    this.invoiceService.getInvoiceStatusData().subscribe({
      next: (response) => {
        this.invoiceStatusData = response.data.invoiceStatusData;
        this.createDoughnutChart();
      },
      error: (error) => {
        console.error('Error fetching average unit consumption', error);
      },
    });
  }

  createBarChart() {
    const labels = Object.keys(this.averageConsumption);
    const dataValues = Object.values(this.averageConsumption);
  
    const barChartElement = document.getElementById('barChart');
    if (!barChartElement) {
      console.error('Element with id "barChart" not found');
      return;
    }
  
    // Check if there's no data
    if (labels.length === 0 || dataValues.every(value => value === 0)) {
      barChartElement.innerHTML = 'No Data Available';
      return;
    }
  
    this.barChart = new Chart('barChart', {
      type: 'bar',
  
      data: {
        labels: labels,
        datasets: [
          {
            label: 'Average Consumption',
            data: dataValues,
            backgroundColor: '#1db386',
          },
        ],
      },
      options: {
        aspectRatio: 1.4,
        responsive: true,
      },
    });
  }
  
  createDoughnutChart() {
    const labels = Object.keys(this.invoiceStatusData);
    const dataValues = Object.values(this.invoiceStatusData);
  
    const doughnutChartElement = document.getElementById('doughnutChart');
    if (!doughnutChartElement) {
      console.error('Element with id "doughnutChart" not found');
      return;
    }
  
    // Check if there's no data
    if (labels.length === 0 || dataValues.every(value => value === 0)) {
      doughnutChartElement.innerHTML = 'No Data Available';
      return;
    }
  
    this.doughnutChart = new Chart('doughnutChart', {
      type: 'doughnut',
  
      data: {
        labels: labels,
        datasets: [
          {
            label: 'Invoice Status',
            data: dataValues,
            backgroundColor: [
              'rgb(255, 99, 132)',
              'rgb(54, 162, 235)',
              'rgb(255, 205, 86)',
            ],
          },
        ],
      },
      options: {
        aspectRatio: 1.4,
        responsive: true,
      },
    });
  }
}  
  