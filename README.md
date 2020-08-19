# Hypertrace Core View Generator

###### org.hypertrace.core.viewgenerator

[![CircleCI](https://circleci.com/gh/hypertrace/hypertrace-core-view-generator.svg?style=svg)](https://circleci.com/gh/hypertrace/hypertrace-core-view-generator)

This repository contains: *1) Hypertrace view creator job 2) Hypertrace view generation service*

Hypertrace view creator:
It is a bootstrap job that creates required views in pinot like spanEventView, backendEntityView, etc.

Hypertrace view generator: 
It is a streaming job that materializes enriched traces into pinot views
