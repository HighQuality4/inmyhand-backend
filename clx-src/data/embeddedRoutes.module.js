const embeddedRoutes = [
  { path: "/", loadApp: "app/home/home" },
  { path: "/recipe", loadApp: "app/recipe/recipe_main" },
  { path: /^\/recipe\/(\d+)$/, loadApp: "app/recipe/recipe_detail" },
  { path: "/recipe/search", loadApp: "app/recipe/recipe_search" },
  { path: "/auth/login", loadApp: "app/login/login" },
  { path: "/users/mypage", loadApp: "app/mypage/mypage"},
  { path: "/users/payment", loadApp: "app/payment/paycheck"},
  { path: "/users/myinfo" , loadApp: "app/mypage/myinfo"},
  { path: "/fridge", loadApp: "app/fridge/fridge_main" },
  { path: "/recipe/form", loadApp: "app/recipe/recipe_form" }, // 레시피 등록
  { path: /^\/recipe\/form\/(\d+)$/, loadApp: "app/recipe/recipe_form" }, // 레시피 수정
  { path: "/auth/register", loadApp: "app/login/register"},
  { path: "/fridge", loadApp: "app/fridge/fridge_main" },
  { path: "/fridge/auto", loadApp: "app/fridge/fridge_auto" }
];

exports.embeddedRoutes = embeddedRoutes;