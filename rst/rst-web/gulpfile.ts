"use strict";

const gulp = require('gulp');
const del = require('del');
const newer = require('gulp-newer');
const autoprefixer = require('gulp-autoprefixer');
const sass = require('gulp-sass');
const sourcemaps = require('gulp-sourcemaps');
const htmlmin = require('gulp-htmlmin');
const less = require('gulp-less');
const minifyCSS = require('gulp-csso');
const cache = require('gulp-cache');
const imagemin = require('gulp-imagemin');
const filter = require('gulp-filter');
const cleanCSS = require('gulp-clean-css');

const webapp = 'build/webapp/';
const dist = 'dist/';

gulp.task('clean', (cb) => {
  return del([webapp + '*', '!' + webapp + 'WEB-INF', '!' + webapp + 'META-INF'], {force: true}, cb);
});

gulp.task('compile', () => {
  return gulp.src(dist + '**/*.js')
    .pipe(newer({dest: webapp, ext: '.js'}))
    .pipe(sourcemaps.init())
    .pipe(sourcemaps.write('/'))
    .pipe(gulp.dest(webapp))
});

gulp.task('html', () => {
  return gulp.src(dist + '**/*.html')
    .pipe(newer(webapp))
    .pipe(sourcemaps.init())
    .pipe(htmlmin({collapseWhitespace: true, caseSensitive: true}))
    .pipe(sourcemaps.write('/'))
    .pipe(gulp.dest(webapp))
});

gulp.task('scss', () => {
  return gulp.src(dist + '**/*.{scss}')
    .pipe(newer({dest: webapp, ext: '.css'}))
    .pipe(sourcemaps.init())
    .pipe(sass({outputStyle: 'compressed'}))
    .pipe(autoprefixer({
      browsers: ['last 2 versions'],
      cascade: false
    }))
    .pipe(sourcemaps.write('/'))
    .pipe(gulp.dest(webapp))
});

gulp.task('css', function () {
  return gulp.src(dist + '**/*.css')
    .pipe(sourcemaps.init())
    .pipe(autoprefixer({
      browsers: ['last 2 versions'],
      cascade: false
    }))
    .pipe(cleanCSS({debug: true}, function (details) {
      console.log(details.name + ': ' + details.stats.originalSize);
      console.log(details.name + ': ' + details.stats.minifiedSize);
    }))
    .pipe(sourcemaps.write())
    .pipe(gulp.dest(webapp))
});

gulp.task('less', function () {
  return gulp.src(dist + '**/*.less')
    .pipe(less())
    .pipe(minifyCSS())
    .pipe(gulp.dest(webapp))
});

gulp.task('images', function () {
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

gulp.task('fonts', function () {
  return gulp.src(dist + '**/*.{eot,svg,ttf,woff,woff2}')
    .pipe(gulp.dest(webapp));
});

gulp.task('build', ['clean'], function () {
  gulp.start(['compile', 'html', 'scss', 'css', 'less', 'images', 'fonts']);
});

gulp.task('watch', function () {
  gulp.watch(dist + '**/*.ts', ['compile']);
  gulp.watch(dist + '**/*.html', ['html']);
  gulp.watch(dist + '**/*.scss', ['css']);
});
