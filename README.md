# Fitness Tracker Application

A comprehensive fitness tracking web application built with Spring Boot, Thymeleaf, and MySQL. This application allows users to track their workouts, monitor progress, and maintain their fitness goals.

## Features

### Core Features
- **User Authentication & Registration**: Secure user registration and login system
- **Dashboard**: Overview of fitness statistics and recent activities
- **Workout Management**: Add, edit, view, and delete workout sessions
- **Progress Tracking**: Monitor workout frequency and calories burned
- **Responsive Design**: Modern, mobile-friendly interface

### User Management
- User registration with profile information
- Secure password encryption
- User profile management
- Session management

### Workout Tracking
- Multiple workout types (Cardio, Strength, Flexibility, Sports, Other)
- Duration and calorie tracking
- Workout notes and descriptions
- Date-based workout history

### Statistics & Analytics
- Weekly and monthly workout summaries
- Total calories burned tracking
- Workout frequency analysis
- Visual progress indicators

## Technology Stack

### Backend
- **Spring Boot 3.2.0**: Main framework
- **Spring Security**: Authentication and authorization
- **Spring Data JPA**: Database operations
- **Hibernate**: ORM framework
- **MySQL**: Database
- **Maven**: Dependency management

### Frontend
- **Thymeleaf**: Server-side templating
- **Bootstrap 5.3.0**: CSS framework
- **Font Awesome**: Icons
- **JavaScript**: Client-side functionality

### Database
- **MySQL 8.0**: Primary database
- **Hibernate DDL**: Automatic schema generation

## Project Structure

```
fitness_tracker/
├── src/
│   ├── main/
│   │   ├── java/com/fitness/
│   │   │   ├── config/
│   │   │   │   ├── SecurityConfig.java
│   │   │   │   └── DataInitializer.java
│   │   │   ├── controller/
│   │   │   │   ├── AuthController.java
│   │   │   │   ├── HomeController.java
│   │   │   │   └── WorkoutController.java
│   │   │   ├── model/
│   │   │   │   ├── User.java
│   │   │   │   └── Workout.java
│   │   │   ├── repository/
│   │   │   │   ├── UserRepository.java
│   │   │   │   └── WorkoutRepository.java
│   │   │   ├── service/
│   │   │   │   ├── UserService.java
│   │   │   │   ├── WorkoutService.java
│   │   │   │   └── CustomUserDetailsService.java
│   │   │   └── FitnessTrackerApplication.java
│   │   └── resources/
│   │       ├── static/
│   │       │   ├── css/
│   │       │   │   └── style.css
│   │       │   └── js/
│   │       │       └── script.js
│   │       ├── templates/
│   │       │   ├── auth/
│   │       │   │   ├── login.html
│   │       │   │   └── register.html
│   │       │   ├── layout/
│   │       │   │   └── base.html
│   │       │   ├── workout/
│   │       │   │   ├── list.html
│   │       │   │   └── form.html
│   │       │   ├── dashboard.html
│   │       │   └── about.html
│   │       └── application.properties
│   └── test/
└── pom.xml
```

## Getting Started

### Prerequisites
- Java 17 or higher
- Maven 3.6 or higher
- MySQL 8.0 or higher

### Installation

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd fitness_tracker
   ```

2. **Configure Database**
   - Create a MySQL database named `fitness_tracker`
   - Update database credentials in `src/main/resources/application.properties`
   ```properties
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   ```

3. **Build the project**
   ```bash
   mvn clean install
   ```

4. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

5. **Access the application**
   - Open your browser and navigate to `http://localhost:8082`
   - Register a new account or login with existing credentials

### Database Configuration

The application uses the following default database settings:
- **Database**: `fitness_tracker`
- **Username**: `root`
- **Password**: `r@1234`
- **Port**: `3306`
- **Application Port**: `8082`

Update these settings in `src/main/resources/application.properties` as needed.

### Default User

The application automatically creates a default user on first startup:
- **Username**: `admin`
- **Password**: `admin123`
- **Email**: `admin@fitness.com`

## Usage

### User Registration
1. Navigate to the registration page
2. Fill in your personal information
3. Choose a username and password
4. Submit the form to create your account

### Adding Workouts
1. Login to your account
2. Click "Add New Workout" from the dashboard
3. Fill in workout details:
   - Workout name
   - Workout type
   - Duration
   - Calories burned (auto-calculated)
   - Notes (optional)
4. Save the workout

### Viewing Progress
- **Dashboard**: Overview of recent workouts and statistics
- **Workouts List**: Complete history of all workouts
- **Statistics**: Weekly and monthly summaries

## API Endpoints

### Authentication
- `GET /login` - Login page
- `POST /login` - Process login
- `GET /register` - Registration page
- `POST /register` - Process registration
- `POST /logout` - Logout

### Dashboard
- `GET /` - Redirect to dashboard
- `GET /dashboard` - Main dashboard

### Workouts
- `GET /workouts` - List all workouts
- `GET /workouts/new` - New workout form
- `POST /workouts` - Create workout
- `GET /workouts/{id}` - View workout
- `GET /workouts/{id}/edit` - Edit workout form
- `POST /workouts/{id}` - Update workout
- `POST /workouts/{id}/delete` - Delete workout

## Security Features

- **Password Encryption**: BCrypt password hashing
- **Session Management**: Secure session handling
- **CSRF Protection**: Disabled for development (can be enabled for production)
- **Input Validation**: Server-side validation
- **SQL Injection Prevention**: Parameterized queries
- **Custom UserDetailsService**: Integration with User entity

## Customization

### Adding New Workout Types
1. Update the `WorkoutType` enum in `Workout.java`
2. Add corresponding CSS classes in `style.css`
3. Update the calorie calculation in `script.js`

### Styling
- Modify `src/main/resources/static/css/style.css` for custom styling
- Update Bootstrap classes in templates for layout changes

### Functionality
- Add new controllers in the `controller` package
- Create new services in the `service` package
- Add new repositories in the `repository` package

## Troubleshooting

### Common Issues

1. **Database Connection Error**
   - Ensure MySQL is running
   - Verify database credentials in `application.properties`
   - Check if the database `fitness_tracker` exists

2. **Port Already in Use**
   - Change the port in `application.properties`: `server.port=8083`
   - Or stop the application using the current port

3. **Spring Security Issues**
   - The application uses custom UserDetailsService for authentication
   - Default user is created automatically on first startup

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Support

For support and questions, please open an issue in the repository or contact the development team.

## Future Enhancements

- [ ] Goal setting and tracking
- [ ] Workout templates
- [ ] Social features (sharing, following)
- [ ] Mobile app integration
- [ ] Advanced analytics and charts
- [ ] Nutrition tracking
- [ ] Exercise library
- [ ] Progress photos
- [ ] Achievement badges
- [ ] Export functionality 