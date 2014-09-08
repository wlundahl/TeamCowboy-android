package walter.teamcowboy.android.tasks;

import com.google.common.collect.ImmutableList;

import java.util.List;

public abstract class TCListAsyncTaskBase<ListType> extends TCAsyncTaskBase<List<ListType>>{
    @Override
    protected boolean isValidData(List<ListType> data) {
        return data != null && !data.isEmpty();
    }

    @Override
    protected List<ListType> getDefaultValue() {
        return ImmutableList.of();
    }
}
