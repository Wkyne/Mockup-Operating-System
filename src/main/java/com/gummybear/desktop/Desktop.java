package com.gummybear.desktop;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
public class Desktop {

    @Getter
    @Setter
    public ContextMenu contextMenu = new ContextMenu();

}
