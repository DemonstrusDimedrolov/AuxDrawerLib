<p align="center">
  <a href="https://jitpack.io/#DemonstrusDimedrolov/AuxDrawerLib">
    <img src="https://jitpack.io/v/DemonstrusDimedrolov/AuxDrawerLib.svg" alt="JitPack Badge" />
  </a>
</p>

<p align="center"><strong>ЭТО МОЕ, НЕ ЮЗАТЬ НИКОМУ</strong></p>


```kotlin
@Composable
fun BasicDrawer() {
    AuxDrawer(
        drawerContent = { _, close ->
            Column {
                Text("Drawer контент", Modifier.padding(16.dp))
                Button(onClick = close) {
                    Text("Закрыть")
                }
            }
        },
        mainContent = { onMenuClick, _, _ ->
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("Basic Drawer") },
                        navigationIcon = {
                            IconButton(onClick = onMenuClick) {
                                Icon(Icons.Default.Menu, contentDescription = null)
                            }
                        }
                    )
                }
            ) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Основной экран")
                }
            }
        }
    )
}
```

```kotlin
@Composable
fun AnimatedDrawer() {
    AuxDrawer(
        animationDurationMillis = 400,
        animationEasing = FastOutSlowInEasing,
        drawerContent = { _, close ->
            Column {
                Text("Медленная анимация", Modifier.padding(16.dp))
                Button(onClick = close) {
                    Text("Закрыть")
                }
            }
        },
        mainContent = { onMenuClick, _, _ ->
            TopBarContent("Animated Drawer", onMenuClick)
        }
    )
}
```

```kotlin
@Composable
fun NoSwipeDrawer() {
    AuxDrawer(
        enableSwipe = false,
        drawerContent = { _, close ->
            Column {
                Text("Свайп отключен", Modifier.padding(16.dp))
                Button(onClick = close) {
                    Text("Закрыть")
                }
            }
        },
        mainContent = { onMenuClick, _, _ ->
            TopBarContent("No Swipe Drawer", onMenuClick)
        }
    )
}
```

```kotlin
@Composable
fun RoundedShadowDrawer() {
    AuxDrawer(
        cornerRadius = 24.dp,
        drawerElevation = 8.dp,
        drawerBackgroundColor = MaterialTheme.colorScheme.tertiaryContainer,
        drawerContent = { _, close ->
            Column {
                Text("Скругления и тень", Modifier.padding(16.dp))
                Button(onClick = close) {
                    Text("Закрыть")
                }
            }
        },
        mainContent = { onMenuClick, _, _ ->
            TopBarContent("Rounded Shadow Drawer", onMenuClick)
        }
    )
}
```

```kotlin
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
private fun TopBarContent(title: String, onMenuClick: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title) },
                navigationIcon = {
                    IconButton(onClick = onMenuClick) {
                        Icon(Icons.Default.Menu, contentDescription = null)
                    }
                }
            )
        }
    ) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Основной контент: $title")
        }
    }
}
```
