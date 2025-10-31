# GitHub Actions Workflows Guide

## 📋 Overview

This repository uses GitHub Actions for automated building, testing, and releasing of the Type Kannada Android application.

## 🔧 Workflows

### 1. Android CI Build (`android-build.yml`)

**Triggers**:
- Push to `master`, `main`, or `develop` branches
- Pull requests to these branches
- Manual trigger via workflow_dispatch

**What it does**:
- ✅ Builds Debug APK
- ✅ Builds Release APK (unsigned)
- ✅ Uploads artifacts for 30 days
- ✅ Caches Gradle dependencies

**Artifacts**:
- `app-debug` - Debug APK for testing
- `app-release-unsigned` - Unsigned release APK

**Usage**:
```bash
# Automatically runs on push
git push origin master

# Or trigger manually from GitHub Actions tab
```

---

### 2. Android Release Build (`android-release.yml`)

**Triggers**:
- Push of version tags (e.g., `v3.7`, `v3.8`)
- Manual trigger via workflow_dispatch

**What it does**:
- ✅ Builds Debug and Release APKs
- ✅ Signs Release APK (if keystore configured)
- ✅ Creates GitHub Release
- ✅ Uploads APKs to release
- ✅ Generates release notes

**Artifacts**:
- `TypeKannada-vX.X-debug.apk`
- `TypeKannada-vX.X-release.apk` (signed if keystore available)

**Usage**:
```bash
# Create and push a tag
git tag v3.8
git push origin v3.8

# Release will be created automatically
```

---

### 3. Pull Request Checks (`pr-check.yml`)

**Triggers**:
- Pull request opened, synchronized, or reopened

**What it does**:
- ✅ Runs code lint checks
- ✅ Builds Debug and Release APKs
- ✅ Runs Gradle check tasks
- ✅ Uploads lint and test results

**Artifacts**:
- `lint-results` - Lint check HTML reports
- `test-results` - Test result XML files

**Usage**:
```bash
# Automatically runs on PR creation
# Check results in PR "Checks" tab
```

---

### 4. Nightly Build (`nightly-build.yml`)

**Triggers**:
- Scheduled: Daily at 2:00 AM UTC
- Manual trigger via workflow_dispatch

**What it does**:
- ✅ Builds Debug APK nightly
- ✅ Names with date (e.g., TypeKannada-nightly-20251031.apk)
- ✅ Keeps last 3 builds, deletes older

**Artifacts**:
- `nightly-build-YYYYMMDD` - Dated nightly build

**Usage**:
```bash
# Runs automatically daily
# Or trigger manually from GitHub Actions tab
```

---

## 📦 APK Builds

All builds are **unsigned** by default. This is normal for open-source projects.

### Build Types

- **Debug APK**: For development and testing
- **Release APK**: Unsigned production build

### Manual Signing (If Needed)

To sign APKs manually after download:

```bash
# Sign using jarsigner
jarsigner -verbose -sigalg SHA1withRSA -digestalg SHA1 \
  -keystore your-keystore.jks \
  app-release-unsigned.apk \
  your-key-alias

# Verify signature
jarsigner -verify -verbose app-release-unsigned.apk
```

---

## 📦 Artifacts

### Where to Find

1. Go to **GitHub Actions** tab
2. Click on a workflow run
3. Scroll to **Artifacts** section
4. Download APKs

### Artifact Retention

| Workflow | Retention |
|----------|-----------|
| CI Build | 30 days |
| Release | 90 days |
| PR Check | 7 days |
| Nightly | 7 days (keeps last 3) |

---

## 🚀 Usage Examples

### Build Debug APK

```bash
# Option 1: Push to master
git add .
git commit -m "Update feature"
git push origin master

# Option 2: Manual trigger
# Go to Actions > Android CI Build > Run workflow
```

### Create Release

```bash
# 1. Update version in app/build.gradle
# versionCode 21
# versionName "3.8"

# 2. Commit changes
git add app/build.gradle
git commit -m "Bump version to 3.8"
git push origin master

# 3. Create and push tag
git tag v3.8
git push origin v3.8

# 4. Check GitHub Releases - APK will be there
```

### Test Pull Request

```bash
# 1. Create feature branch
git checkout -b feature/new-font

# 2. Make changes and push
git add .
git commit -m "Add new font style"
git push origin feature/new-font

# 3. Create PR on GitHub
# Checks will run automatically
```

---

## 🔍 Monitoring Builds

### Status Badges

Add to README.md:

```markdown
![Android CI](https://github.com/yourusername/KannadaCustomKeyboard/workflows/Android%20CI%20Build/badge.svg)
![Release](https://github.com/yourusername/KannadaCustomKeyboard/workflows/Android%20Release%20Build/badge.svg)
```

### Email Notifications

GitHub sends email notifications for:
- ✅ Build success
- ❌ Build failure
- 🔄 Workflow status changes

Configure in: **GitHub Settings > Notifications**

---

## 🐛 Troubleshooting

### Build Fails

**Check logs**:
1. Go to Actions tab
2. Click failed workflow
3. Click failed job
4. Expand step to see error

**Common issues**:

**Gradle build failed**:
```bash
# Fix: Update Gradle wrapper locally and commit
./gradlew wrapper --gradle-version 7.4
git add gradle/wrapper/*
git commit -m "Update Gradle wrapper"
```

**Dependency issues**:
```bash
# Fix: Clear Gradle cache
# In workflow, this is automatic via cache action
# Locally:
./gradlew clean --no-daemon
```

**Out of memory**:
```yaml
# Add to workflow (already included):
- name: Build with more memory
  run: ./gradlew assembleDebug --stacktrace
  env:
    GRADLE_OPTS: -Xmx4096m
```

### Signing Fails

**Check**:
- All 4 secrets are set correctly
- Keystore base64 is complete (no line breaks)
- Passwords are correct

**Debug**:
```bash
# Verify keystore locally
keytool -list -v -keystore release-key.jks

# Test signing locally
./gradlew assembleRelease \
  -Pandroid.injected.signing.store.file=release-key.jks \
  -Pandroid.injected.signing.store.password=your-password \
  -Pandroid.injected.signing.key.alias=your-alias \
  -Pandroid.injected.signing.key.password=your-password
```

### Artifact Upload Fails

**Increase retention**:
```yaml
- name: Upload APK
  uses: actions/upload-artifact@v3
  with:
    name: app-debug
    path: app/build/outputs/apk/debug/app-debug.apk
    retention-days: 90  # Increase from 30
```

---

## ⚡ Performance Optimization

### Gradle Caching

Already configured in workflows:

```yaml
- name: Cache Gradle
  uses: actions/cache@v3
  with:
    path: |
      ~/.gradle/caches
      ~/.gradle/wrapper
    key: ${{ runner.os }}-gradle-${{ hashFiles('**/*.gradle*') }}
```

**Speeds up builds by ~2-3 minutes**

### Parallel Builds

Gradle builds in parallel by default. To adjust:

```yaml
- name: Build APK
  run: ./gradlew assembleDebug --parallel --max-workers=4
```

---

## 🔄 Workflow Dependencies

```
android-build.yml (CI)
  ↓
  Runs on every push/PR
  ↓
  Validates builds work

pr-check.yml (Quality)
  ↓
  Runs on PR
  ↓
  Ensures code quality

android-release.yml (Release)
  ↓
  Runs on version tags
  ↓
  Creates GitHub Release

nightly-build.yml (Testing)
  ↓
  Runs daily at 2 AM UTC
  ↓
  Continuous integration testing
```

---

## 📊 Workflow Matrix (Advanced)

To test multiple Android API levels:

```yaml
jobs:
  build:
    strategy:
      matrix:
        api-level: [18, 26, 30, 33]
    steps:
      - name: Build for API ${{ matrix.api-level }}
        run: ./gradlew assembleDebug -PtargetSdk=${{ matrix.api-level }}
```

---

## 🎯 Best Practices

### 1. Version Tagging
```bash
# Use semantic versioning
v3.7.0 - Major.Minor.Patch
v3.8.0 - New features
v3.8.1 - Bug fixes
```

### 2. Commit Messages
```bash
# Good
git commit -m "Add font style selector feature"
git commit -m "Fix crash on keyboard rotation"

# Bad
git commit -m "Update"
git commit -m "Changes"
```

### 3. Branch Strategy
```
master/main   → Production releases (v3.7, v3.8)
develop       → Development/staging
feature/*     → New features
bugfix/*      → Bug fixes
hotfix/*      → Emergency fixes
```

### 4. Release Notes
When creating tags, add release notes:

```bash
git tag -a v3.8 -m "Release v3.8

Features:
- Font style customization
- Improved keyboard performance

Bug Fixes:
- Fixed crash on Android 13
- Fixed memory leak in preview
"
git push origin v3.8
```

---

## 📝 Customization

### Add New Workflow

Create: `.github/workflows/your-workflow.yml`

```yaml
name: Your Workflow

on:
  push:
    branches: [ master ]

jobs:
  your-job:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - name: Your step
        run: echo "Hello"
```

### Modify Existing Workflow

Edit `.github/workflows/*.yml` and commit:

```bash
git add .github/workflows/android-build.yml
git commit -m "Update build workflow"
git push
```

---

## 🔗 Useful Links

- [GitHub Actions Documentation](https://docs.github.com/en/actions)
- [Android CI/CD Guide](https://developer.android.com/studio/publish/app-signing)
- [Workflow Syntax](https://docs.github.com/en/actions/using-workflows/workflow-syntax-for-github-actions)

---

## ✅ Checklist

Before pushing to GitHub:

- [ ] Workflows folder exists: `.github/workflows/`
- [ ] All 4 workflow files present
- [ ] `gradlew` has execute permission
- [ ] Version tags follow semantic versioning
- [ ] (Optional) Keystore secrets configured
- [ ] Repository settings allow Actions

---

## 📞 Support

If workflows fail:
1. Check Actions tab for error logs
2. Review Troubleshooting section above
3. Check workflow YAML syntax
4. Verify repository settings allow Actions

---

*Last Updated: 2025-10-31*
