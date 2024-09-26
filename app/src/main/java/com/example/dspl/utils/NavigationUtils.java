package com.example.dspl.utils;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.IdRes;
import androidx.navigation.NavAction;
import androidx.navigation.NavController;
import androidx.navigation.NavDeepLinkRequest;
import androidx.navigation.NavDestination;
import androidx.navigation.NavDirections;
import androidx.navigation.NavGraph;
import androidx.navigation.NavOptions;

import com.example.dspl.R;


public class NavigationUtils {

    /**
     * This function will check navigation safety before starting navigation using direction
     *
     * @param navController NavController instance
     * @param direction     navigation operation
     */
    public static void navigateSafe(NavController navController, NavDirections direction) {
        NavDestination currentDestination = navController.getCurrentDestination();

        if (currentDestination != null) {
            NavAction navAction = currentDestination.getAction(direction.getActionId());

            if (navAction != null) {
                int destinationId = orEmpty(navAction.getDestinationId());

                NavGraph currentNode;
                if (currentDestination instanceof NavGraph)
                    currentNode = (NavGraph) currentDestination;
                else
                    currentNode = currentDestination.getParent();

                if (destinationId != 0 && currentNode != null && currentNode.findNode(destinationId) != null) {
                    navController.navigate(direction);
                }
            }
        }
    }

    /**
     * This function will check navigation safety before starting navigation using direction & will
     * use default animation for Navigation Actions
     *
     * @param navController NavController instance
     * @param direction     navigation operation
     * @param popBackStack  to handle transaction animation
     */
    public static void navigateSafeWithAnim(NavController navController, NavDirections direction,boolean... popBackStack) {
        NavDestination currentDestination = navController.getCurrentDestination();

        if (currentDestination != null) {
            NavAction navAction = currentDestination.getAction(direction.getActionId());

            if (navAction != null) {
                int destinationId = orEmpty(navAction.getDestinationId());

                NavGraph currentNode;
                if (currentDestination instanceof NavGraph)
                    currentNode = (NavGraph) currentDestination;
                else
                    currentNode = currentDestination.getParent();

                if (destinationId != 0 && currentNode != null && currentNode.findNode(destinationId) != null) {
                    boolean backStackFlag = popBackStack.length >= 1 && popBackStack[0];
                    navController.navigate(direction,backStackFlag ? getBackPressAnimation():getNavGraphAnimation());
                }
            }
        }
    }

    /**
     * This function will check navigation safety before starting navigation using direction & will
     * use default animation for Navigation Actions
     *
     * @param navController NavController instance
     * @param resId         destination resource id
     * @param args          bundle arguments
     * @param popBackStack  to handle transaction animation
     */
    public static void navigateSafeWithAnim(NavController navController, @IdRes int resId,Bundle args,boolean... popBackStack) {
        NavDestination currentDestination = navController.getCurrentDestination();

        if (currentDestination != null) {
            NavAction navAction = currentDestination.getAction(resId);

            if (navAction != null) {
                int destinationId = orEmpty(navAction.getDestinationId());

                NavGraph currentNode;
                if (currentDestination instanceof NavGraph)
                    currentNode = (NavGraph) currentDestination;
                else
                    currentNode = currentDestination.getParent();

                if (destinationId != 0 && currentNode != null && currentNode.findNode(destinationId) != null) {
                    boolean backStackFlag = popBackStack.length >= 1 && popBackStack[0];
                    navController.navigate(resId,args,backStackFlag ? getBackPressAnimation():getNavGraphAnimation());
                }
            }
        }
    }

    /**
     * This function will check navigation safety before starting navigation using resId and args bundle
     *
     * @param navController NavController instance
     * @param resId         destination resource id
     * @param args          bundle args
     */
    public static void navigateSafe(NavController navController, @IdRes int resId, Bundle args) {
        NavDestination currentDestination = navController.getCurrentDestination();

        if (currentDestination != null) {
            NavAction navAction = currentDestination.getAction(resId);

            if (navAction != null) {
                int destinationId = orEmpty(navAction.getDestinationId());

                NavGraph currentNode;
                if (currentDestination instanceof NavGraph)
                    currentNode = (NavGraph) currentDestination;
                else
                    currentNode = currentDestination.getParent();

                if (destinationId != 0 && currentNode != null && currentNode.findNode(destinationId) != null) {
                    navController.navigate(resId, args);
                }
            }
        }
    }

    private static int orEmpty(Integer value) {
        return value == null ? 0 : value;
    }

    private static NavDeepLinkRequest buildDeepLink(String address){
      return  NavDeepLinkRequest.Builder
                .fromUri(Uri.parse(address))
                .build();

    }

    public static void deepLinkNavigateTo(NavController navController ,String address, Boolean popUpTo){
        NavOptions.Builder builder = new NavOptions.Builder();

        if (popUpTo) {
            builder.setPopUpTo(navController.getGraph().getStartDestination(), true);
        }

        navController.navigate(
                buildDeepLink(address),
                builder.build()
        );
    }

    public static NavOptions getNavGraphAnimation(){
        return new  NavOptions.Builder()
                        .setEnterAnim(R.anim.slide_in_right)
                        .setExitAnim(R.anim.slide_out_left)
                        .setPopEnterAnim(R.anim.slide_in_left)
                        .setPopExitAnim(R.anim.slide_out_right)
                        .build();
    }

    public static NavOptions getBackPressAnimation(){
        return new  NavOptions.Builder()
                .setEnterAnim(R.anim.slide_out_left)
                .setExitAnim(R.anim.slide_in_right)
                .setPopEnterAnim(R.anim.slide_out_right)
                .setPopExitAnim(R.anim.slide_in_left)
                .build();
    }
}
