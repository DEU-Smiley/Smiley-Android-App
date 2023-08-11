package com.example.smiley.common.extension

import com.facebook.shimmer.ShimmerFrameLayout


fun ShimmerFrameLayout.start(){
    this.startShimmer()
    this.visible()
}

fun ShimmerFrameLayout.stop(){
    this.stopShimmer()
    this.invisible()
}