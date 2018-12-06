"use strict";
var gulp = require('gulp');
var del = require('del');
var ts = require('gulp-typescript');
var newer = require('gulp-newer');
var autoprefixer = require('gulp-autoprefixer');
var sass = require('gulp-sass');
var sourcemaps = require('gulp-sourcemaps');
var htmlmin = require('gulp-htmlmin');
var less = require('gulp-less');
var minifyCSS = require('gulp-csso');
var cache = require('gulp-cache');
var imagemin = require('gulp-imagemin');
var filter = require('gulp-filter');
var cleanCSS = require('gulp-clean-css');
var webapp = 'build/webapp/';
var dist = 'dist/';
gulp.task('clean', function(cb) {
    return del([webapp + '*', '!' + webapp + 'WEB-INF', '!' + webapp + 'META-INF'], { force: true }, cb);
});
gulp.task('compile', function() {
    return gulp.src(dist + '**/*.js')
        .pipe(newer({ dest: webapp, ext: '.js' }))
        .pipe(sourcemaps.init())
        .pipe(sourcemaps.write('/'))
        .pipe(gulp.dest(webapp));
});
gulp.task('html', function() {
    return gulp.src(dist + '**/*.html')
        .pipe(newer(webapp))
        .pipe(sourcemaps.init())
        .pipe(htmlmin({ collapseWhitespace: true, caseSensitive: true }))
        .pipe(sourcemaps.write('/'))
        .pipe(gulp.dest(webapp));
});
gulp.task('scss', function() {
    return gulp.src(dist + '**/*.{scss}')
        .pipe(newer({ dest: webapp, ext: '.css' }))
        .pipe(sourcemaps.init())
        .pipe(sass({ outputStyle: 'compressed' }))
        .pipe(autoprefixer({
            browsers: ['last 2 versions'],
            cascade: false
        }))
        .pipe(sourcemaps.write('/'))
        .pipe(gulp.dest(webapp));
});
gulp.task('css', function() {
    return gulp.src(dist + '**/*.css')
        .pipe(sourcemaps.init())
        .pipe(autoprefixer({
            browsers: ['last 2 versions'],
            cascade: false
        }))
        .pipe(cleanCSS({ debug: true }, function(details) {
            //console.log(details.name + ': ' + details.stats.originalSize);
            //console.log(details.name + ': ' + details.stats.minifiedSize);
        }))
        .pipe(sourcemaps.write())
        .pipe(gulp.dest(webapp));
});
gulp.task('less', function() {
    return gulp.src(dist + '**/*.less')
        .pipe(less())
        .pipe(minifyCSS())
        .pipe(gulp.dest(webapp));
});
gulp.task('images', function() {
    return gulp.src(dist + '**/*.{gif,jpg,png,svg,ico}')
        .pipe(cache(imagemin({
            progressive: true,
            interlaced: true,
            options: {
                cache: false
            }
        })))
        .pipe(gulp.dest(webapp));
});
gulp.task('fonts', function() {
    return gulp.src(dist + '**/*.{eot,svg,ttf,woff,woff2}')
        .pipe(gulp.dest(webapp));
});
gulp.task('build', ['clean'], function() {
    gulp.start(['compile', 'html', 'scss', 'css', 'less', 'images', 'fonts']);
});
gulp.task('watch', function() {
    gulp.watch(dist + '**/*.ts', ['compile']);
    gulp.watch(dist + '**/*.html', ['html']);
    gulp.watch(dist + '**/*.scss', ['css']);
});