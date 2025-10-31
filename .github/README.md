# GitHub Workflows for Type Kannada

## 🚀 Quick Start

### For Developers

**Build Debug APK**:
```bash
git push origin master
# Wait for workflow to complete
# Download from Actions > Artifacts
```

**Create Release**:
```bash
git tag v3.8
git push origin v3.8
# Release created automatically
# APKs available in Releases section
```

### For Contributors

**Submit Changes**:
```bash
git checkout -b feature/my-feature
# Make changes
git commit -m "Add my feature"
git push origin feature/my-feature
# Create PR - automated checks will run
```

---

## 📚 Available Workflows

| Workflow | Purpose | Trigger |
|----------|---------|---------|
| **Android CI Build** | Build debug/release APKs | Push, PR, Manual |
| **Android Release** | Create GitHub releases | Version tags |
| **PR Checks** | Code quality checks | Pull requests |
| **Nightly Build** | Daily test builds | Schedule, Manual |

---

## 📖 Documentation

**Full Guide**: See [WORKFLOWS_GUIDE.md](./WORKFLOWS_GUIDE.md)

Topics covered:
- Detailed workflow descriptions
- APK signing setup
- Troubleshooting
- Best practices
- Customization

---

## 🔐 Required Secrets (Optional)

For signed releases, add to repository secrets:

- `KEYSTORE_FILE` - Base64 encoded keystore
- `KEY_ALIAS` - Keystore alias
- `KEYSTORE_PASSWORD` - Keystore password
- `KEY_PASSWORD` - Key password

See [WORKFLOWS_GUIDE.md](./WORKFLOWS_GUIDE.md#setup-apk-signing) for details.

---

## 🎯 Status

![Android CI](../../workflows/Android%20CI%20Build/badge.svg)
![Release](../../workflows/Android%20Release%20Build/badge.svg)

---

## 📞 Questions?

Read the [comprehensive guide](./WORKFLOWS_GUIDE.md) or check workflow logs in Actions tab.
