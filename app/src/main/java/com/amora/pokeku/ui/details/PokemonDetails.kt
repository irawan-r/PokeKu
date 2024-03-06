package com.amora.pokeku.ui.details

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.amora.pokeku.R
import com.amora.pokeku.persistence.entity.PokemonCompleteDetails
import com.amora.pokeku.persistence.entity.PokemonCompleteDetails.Companion.getImageUrl
import com.amora.pokeku.repository.model.PokeMark
import com.amora.pokeku.ui.utils.NetworkImage
import kotlin.random.Random

@Composable
fun PokemonDetails(
	poke: PokeMark,
	viewModel: DetailsViewModel,
	pressOnBack: () -> Unit = {}
) {
	LaunchedEffect(key1 = poke) {
		viewModel.loadPosterByName(poke)
	}

	val details by viewModel.posterDetailsFlow.collectAsState(initial = null)
	details?.let {
		PokemonDetailsBody(viewModel = viewModel, poster = it, pressOnBack = pressOnBack)
	}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PokemonDetailsBody(
	viewModel: DetailsViewModel,
	poster: PokemonCompleteDetails,
	pressOnBack: () -> Unit = {}
) {
	val pokemonState by viewModel.getPokemon.collectAsState(initial = null)
	val snackbarHostState = remember { SnackbarHostState() }
	var loadingState by remember { mutableStateOf(false) }
	val randomColorBackground = remember { mutableStateOf(randomColor())  }
	val randomColorSubTitle = remember { mutableStateOf(randomColor())  }
	Scaffold(
		modifier = Modifier.fillMaxSize(),
		snackbarHost = { SnackbarHost(snackbarHostState) }
	) { paddingValues ->
		ConstraintLayout(
			modifier = Modifier
				.fillMaxSize()
				.verticalScroll(rememberScrollState())
				.background(backgroundColor)
				.padding(paddingValues)
		) {
			val (background, arrow, title, species, physics, content, button) = createRefs()
			Box(
				modifier = Modifier
					.background(
						Brush.verticalGradient(
							listOf(
								randomColorBackground.value,
								randomColorBackground.value,
								MaterialTheme.colorScheme.background
							)
						),
						shape = RoundedCornerShape(
							topStart = 0.dp,
							topEnd = 0.dp,
							bottomEnd = 50.dp,
							bottomStart = 50.dp
						)
					)
					.constrainAs(background) {
						top.linkTo(parent.top)
						start.linkTo(parent.start)
						end.linkTo(parent.end)
					}

			) {
				NetworkImage(
					url = poster.getImageUrl(),
					modifier = Modifier
						.aspectRatio(1.5f)
						.background(Color.Transparent),
					circularRevealEnabled = true,
					contentScale = ContentScale.Inside
				)
			}

			Icon(
				imageVector = Icons.Filled.ArrowBack,
				tint = MaterialTheme.colorScheme.onSecondary,
				contentDescription = null,
				modifier = Modifier
					.constrainAs(arrow) {
						top.linkTo(parent.top)
					}
					.padding(12.dp)
					.statusBarsPadding()
					.clickable { pressOnBack() }
			)

			Text(
				text = poster.pokemonDetails.name.toString(),
				modifier = Modifier
					.padding(top = 16.dp)
					.constrainAs(title) {
						top.linkTo(background.bottom)
						linkTo(
							start = parent.start,
							end = parent.end,
							startMargin = 16.dp,
							endMargin = 16.dp
						)
					}
					.padding(8.dp),
				textAlign = TextAlign.Center,
				fontSize = 40.sp,
				fontWeight = FontWeight.Bold,
				color = Color.White
			)

			Row(
				modifier = Modifier
					.constrainAs(species) {
						top.linkTo(title.bottom)
						linkTo(
							start = parent.start,
							end = parent.end,
							startMargin = 16.dp,
							endMargin = 16.dp
						)
					}
					.padding(16.dp)
					.padding(top = 8.dp),
				horizontalArrangement = Arrangement.Center
			) {
				poster.types.forEach {
					Text(
						text = it.type?.name.toString(),
						color = MaterialTheme.colorScheme.background,
						modifier = Modifier
							.padding(horizontal = 8.dp)
							.background(
								randomColorSubTitle.value, shape = RoundedCornerShape(
									topStart = 10.dp,
									topEnd = 10.dp,
									bottomEnd = 10.dp,
									bottomStart = 10.dp
								)
							)
							.padding(horizontal = 16.dp, vertical = 4.dp)
					)
				}
			}

			Row(
				modifier = Modifier
					.constrainAs(physics) {
						top.linkTo(species.bottom)
						linkTo(
							start = parent.start,
							end = parent.end
						)
					}
					.padding(16.dp),
				horizontalArrangement = Arrangement.SpaceEvenly
			) {

				Column(
					modifier = Modifier.padding(horizontal = 32.dp),
					horizontalAlignment = Alignment.CenterHorizontally
				) {
					Text(
						text = poster.pokemonDetails.weight.convertWeight(),
						color = MaterialTheme.colorScheme.background,
						fontWeight = FontWeight.Bold,
						fontSize = 20.sp
					)
					Text(
						text = "Weight",
						color = MaterialTheme.colorScheme.background,
						modifier = Modifier.padding(vertical = 8.dp)
					)
				}

				Column(
					modifier = Modifier.padding(horizontal = 32.dp),
					horizontalAlignment = Alignment.CenterHorizontally
				) {
					Text(
						text = poster.pokemonDetails.height.convertHeight(),
						color = MaterialTheme.colorScheme.background,
						fontWeight = FontWeight.Bold,
						fontSize = 20.sp
					)
					Text(
						text = "Height",
						color = MaterialTheme.colorScheme.background,
						modifier = Modifier.padding(vertical = 8.dp)
					)
				}
			}

			Column(
				modifier = Modifier
					.constrainAs(content) {
						top.linkTo(physics.bottom)
						linkTo(
							start = parent.start,
							end = parent.end
						)
					}
					.padding(horizontal = 32.dp)
					.padding(bottom = 16.dp)
			) {

				Text(
					text = "Base Stats",
					fontSize = 20.sp,
					fontWeight = FontWeight.Bold,
					color = MaterialTheme.colorScheme.background, modifier = Modifier
						.align(Alignment.CenterHorizontally)
						.padding(bottom = 20.dp)
				)

				poster.stats.forEach { item ->
					Row(
						modifier = Modifier
							.fillMaxWidth()
							.padding(vertical = 8.dp),
						horizontalArrangement = Arrangement.SpaceBetween
					) {

						val animatedWidth = remember { Animatable(0f) }

						LaunchedEffect(Unit) {
							animatedWidth.animateTo(
								targetValue = item.baseStat?.toFloat() ?: 0f,
								animationSpec = tween(durationMillis = 1000)
							)
						}

						Text(
							text = item.stats?.name.toString(),
							color = MaterialTheme.colorScheme.background,
							modifier = Modifier
								.padding(end = 16.dp)
								.width(120.dp),
							fontSize = 16.sp
						)

						Canvas(
							modifier = Modifier.fillMaxWidth(),
							onDraw = {
								val cornerRadius = CornerRadius(16f, 16f)
								val rectOne = Path().apply {
									addRoundRect(
										RoundRect(
											rect = Rect(
												offset = Offset(0f, 0f),
												size = Size(
													item.baseStat?.toFloat()!!,
													20.dp.toPx()
												),
											),
											bottomLeft = cornerRadius,
											topLeft = cornerRadius
										)
									)
								}
								drawPath(rectOne, color = Color.Green)

								val rectTwo = Path().apply {
									addRoundRect(
										RoundRect(
											rect = Rect(
												offset = Offset(
													item.baseStat?.toFloat()!! + 4f,
													0f
												),
												size = Size(
													size.width - item.baseStat.toFloat(),
													20.dp.toPx()
												),
											),
											topRight = cornerRadius,
											bottomRight = cornerRadius,
										)
									)
								}
								drawPath(rectTwo, color = Color.White)

								val percentageText = "${item.baseStat}/${size.width.toInt()}"
								val paint = Paint().asFrameworkPaint().apply {
									color = Color.Black.toArgb()
									textSize = 20f
								}
								val textHeight = paint.descent() - paint.ascent()
								val centerY = (size.height + textHeight)

								drawContext.canvas.nativeCanvas.drawText(
									percentageText,
									item.baseStat?.toFloat()!! + 6f,
									centerY, // y-coordinate (adjust as needed)
									paint
								)
							}
						)
					}
				}

				Row(
					modifier = Modifier
						.fillMaxWidth()
						.padding(vertical = 8.dp),
					horizontalArrangement = Arrangement.Start
				) {
					Text(
						text = "exp",
						color = MaterialTheme.colorScheme.background,
						modifier = Modifier
							.padding(end = 16.dp)
							.width(120.dp)
					)

					Canvas(
						modifier = Modifier.fillMaxWidth(),
						onDraw = {
							val cornerRadius = CornerRadius(16f, 16f)
							val rectOne = Path().apply {
								addRoundRect(
									RoundRect(
										rect = Rect(
											offset = Offset(0f, 0f),
											size = Size(
												poster.pokemonDetails.baseExperience?.toFloat()!!,
												20.dp.toPx()
											),
										),
										bottomLeft = cornerRadius,
										topLeft = cornerRadius
									)
								)
							}
							drawPath(rectOne, color = Color.Green)

							val rectTwo = Path().apply {
								addRoundRect(
									RoundRect(
										rect = Rect(
											offset = Offset(
												poster.pokemonDetails.baseExperience?.toFloat()!! + 4f,
												0f
											),
											size = Size(
												size.width - poster.pokemonDetails.baseExperience.toFloat(),
												20.dp.toPx()
											),
										),
										topRight = cornerRadius,
										bottomRight = cornerRadius,
									)
								)
							}
							drawPath(rectTwo, color = Color.White)

							val percentageText =
								"${poster.pokemonDetails.baseExperience}/${size.width.toInt()}"
							val paint = Paint().asFrameworkPaint().apply {
								color = Color.Black.toArgb()
								textSize = 20f
							}
							val textHeight = paint.descent() - paint.ascent()
							val centerY = (size.height + textHeight)

							drawContext.canvas.nativeCanvas.drawText(
								percentageText,
								poster.pokemonDetails.baseExperience?.toFloat()!! + 6f,
								centerY, // y-coordinate (adjust as needed)
								paint
							)
						}
					)
				}
			}

			if (loadingState) {
				LinearProgressIndicator(modifier = Modifier
					.fillMaxWidth()
					.constrainAs(button) {
						top.linkTo(content.bottom)
						bottom.linkTo(parent.bottom)
					})
			} else {
				Image(
					painter = painterResource(id = R.drawable.ic_pokeballs),
					contentDescription = null, // Provide a content description if needed
					modifier = Modifier
						.size(70.dp)
						.clickable {
							viewModel.catchPokemon()
							loadingState = true
						}
						.constrainAs(button) {
							top.linkTo(content.bottom)
							bottom.linkTo(parent.bottom)
							start.linkTo(parent.start)
							end.linkTo(parent.end)
						}
						.padding(8.dp)
				)
			}
		}
	}

	LaunchedEffect(pokemonState) {
		when {
			pokemonState != null && pokemonState == true -> {
				snackbarHostState.showSnackbar(
					message = "Success, your pokemon has been catched",
					duration = SnackbarDuration.Short
				)
				viewModel.insertPokemonName()
				loadingState = false
				pressOnBack()
			}
			pokemonState != null && pokemonState == false -> {
				snackbarHostState.showSnackbar(
					message = "Failed, try again",
					duration = SnackbarDuration.Short
				)
				pressOnBack()
				loadingState = false
			}
		}
	}
}

private fun Int?.convertWeight(): String {
	val weight = this?.toDouble()?.div(10)
	return "$weight KG"
}

private fun Int?.convertHeight(): String {
	val height = this?.toDouble()?.div(10)
	return "$height M"
}

private fun randomColor(): Color {
	val random = Random
	return Color(random.nextInt(256), random.nextInt(256), random.nextInt(256))
}
