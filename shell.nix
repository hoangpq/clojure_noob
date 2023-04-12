with import <nixpkgs> {};

pkgs.mkShell {
  packages = [ libiconv ];
  buildInputs = [ libiconv ];
  nativeBuildInputs = with pkgs; [
    cmake
    gdb
    ninja
    qemu
    ghc
  ] ++ (with llvmPackages_13; [
    clang
    clang-unwrapped
    lld
    llvm
    darwin.apple_sdk.frameworks.Foundation
  ]);

  hardeningDisable = [ "all" ];
}
