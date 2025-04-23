const embeddedRoutes = [
  { path: "/recipe", loadApp: "app/recipe/recipe_main" }, // 정적 경로
  { path: /^\/recipe\/(\d+)$/, loadApp: "app/recipe/recipe_detail" }, // id 값이 있는 경우
  { path: "/recipe/search", loadApp: "app/recipe/recipe_search" }, // 쿼리 파라미터가 있는 경우
  { path: "/users/login", loadApp: "app/login/login" },
  { path: "/users/mypage", loadApp: "app/mypage/mypage"}
];

exports.embeddedRoutes = embeddedRoutes;