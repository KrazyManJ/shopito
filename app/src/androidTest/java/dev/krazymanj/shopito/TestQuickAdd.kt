package dev.krazymanj.shopito

import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.hasAnyDescendant
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dev.krazymanj.shopito.extension.empty
import dev.krazymanj.shopito.navigation.Destination
import dev.krazymanj.shopito.navigation.NavGraph
import dev.krazymanj.shopito.ui.UITestTag
import dev.krazymanj.shopito.ui.activities.MainActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.runners.MethodSorters

@ExperimentalCoroutinesApi
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@HiltAndroidTest
class TestQuickAdd {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun test_add_item_via_quick_add() {
        launchLoginScreenWithNavigation()
        with(composeRule) {
            // Go to shopping lists
            onNodeWithTag(UITestTag.ShopitoNavigationBar.ShoppingListsScreen)
                .assertIsDisplayed()
                .performClick()
            // Go to shopping list
            onNodeWithTag(UITestTag.ShoppingList.ShoppingList)
                .assertIsDisplayed()
                .performClick()
            // Check if add button is disabled
            val addButton = onNodeWithTag(UITestTag.QuickAdd.AddButton)
                .assertIsDisplayed()
            val textField = onNodeWithTag(UITestTag.QuickAdd.TextField)
                .assertIsDisplayed()


            addButton
                .assertIsNotEnabled()
            textField
                .assert(hasText(String.empty))
                .performTextInput("Milk 2x")
            textField
                .assert(hasText("Milk 2x"))
            addButton
                .assertIsEnabled()
                .performClick()
            textField
                .assert(hasText(String.empty))
            addButton
                .assertIsNotEnabled()

            waitForIdle()
            onNodeWithTag(UITestTag.ShoppingItem.ShoppingItem, useUnmergedTree = true)
                .assert(hasAnyDescendant(
                    hasTestTag(UITestTag.ShoppingItem.Name) and hasText("Milk")
                ))
                .assert(hasAnyDescendant(
                    hasTestTag(UITestTag.ShoppingItem.Amount) and hasText("2x")
                ))
        }
    }

    @OptIn(ExperimentalAnimationApi::class)
    private fun launchLoginScreenWithNavigation() {
        composeRule.activity.setContent {
            MaterialTheme {
                NavGraph(
                    startDestination = Destination.ShoppingListsSummaryScreen
                )
            }
        }
    }
}