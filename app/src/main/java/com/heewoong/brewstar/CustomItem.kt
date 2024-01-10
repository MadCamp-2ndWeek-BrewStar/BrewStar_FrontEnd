package com.heewoong.brewstar

data class CustomItem (val id: String, val category: String, val name: String, val menu: String,  val custom: String, val description: String, val creator: String, val likes: String)

// name: 레몬 아샷추 -> 사용자가 만든 이름
// menu: Iced Caffe Americano -> 실제 메뉴 이름
// category: coffee -> 큰 카테고리 (Coffee, Non Coffee, Frappuccino)
// Custom: 샷2+레몬시럽+복숭아티백 -> 커스텀한 내용
// Description: 이건 달달하고 씁쓸하니 맛있습니다~ -> 설명
// creator: @oliheilnany_ -> 커스텀 만든 사람
// likes: 101 -> 좋아요 수
// wish: O -> 나의 좋아요에 들어가있는가? O, X

// class customDummy (val name : String, val menu : String, val option : String, val creator : String, val likes : Int){}
// class topTenDummy (val name : String, val option : String, val creator : String, val likes : Int){}
// data class FavoriteItem(val name: String, val custom: String)
// data class MyCustomsItem (val name: String, val menu: String, val custom: String, val likes: String) // my custom 형식