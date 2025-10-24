package com.gummybear.desktop.explorer;

import com.gummybear.data.FileDataManager;
import com.gummybear.data.FileDataTree;
import com.gummybear.desktop.window.ExplorerWindow;
import com.gummybear.desktop.window.TerminalWindow;
import com.gummybear.desktop.window.Window;

public class Explorer {

    Window explorerWindow;

    public Explorer() {

        explorerWindow = new ExplorerWindow(FileDataTree.getRootDirectory());

    }
}
